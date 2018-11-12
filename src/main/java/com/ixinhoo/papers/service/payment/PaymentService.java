package com.ixinhoo.papers.service.payment;

import com.chunecai.crumbs.alipay.service.AlipayService;
import com.chunecai.crumbs.api.client.DetailDto;
import com.chunecai.crumbs.api.client.StatusDto;
import com.chunecai.crumbs.code.util.date.DateUtil;
import com.chunecai.crumbs.code.util.http.NetworkUtil;
import com.chunecai.crumbs.code.util.mapper.JsonMapper;
import com.chunecai.crumbs.code.util.number.CodeNumberMaker;
import com.chunecai.crumbs.code.util.number.NumberUtil;
import com.chunecai.crumbs.wechat.dto.WechatOrderPayReqDto;
import com.chunecai.crumbs.wechat.sdk.WXPay;
import com.chunecai.crumbs.wechat.sdk.WXPayUtil;
import com.chunecai.crumbs.wechat.service.WechatMerchantService;
import com.chunecai.crumbs.wechat.util.WechatMerchantConfig;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.ixinhoo.papers.dao.user.UserDao;
import com.ixinhoo.papers.dao.user.UserExtendDao;
import com.ixinhoo.papers.dao.website.FillMoneySettingDao;
import com.ixinhoo.papers.entity.EntitySetting;
import com.ixinhoo.papers.entity.user.User;
import com.ixinhoo.papers.entity.user.UserExtend;
import com.ixinhoo.papers.entity.user.UserIntegral;
import com.ixinhoo.papers.entity.user.UserOrder;
import com.ixinhoo.papers.entity.website.CoinSetting;
import com.ixinhoo.papers.entity.website.FillMoneySetting;
import com.ixinhoo.papers.service.user.UserIntegralService;
import com.ixinhoo.papers.service.user.UserOrderService;
import com.ixinhoo.papers.service.website.CoinSettingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.weekend.WeekendSqls;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.Iterator;
import java.util.Map;

/**
 * 支付相关service
 *
 * @author cici
 */
@Component
public class PaymentService {
    private static Logger logger = LoggerFactory.getLogger(PaymentService.class);
    @Autowired
    private WechatMerchantService wechatMerchantService;
    @Autowired
    private AlipayService alipayService;
    @Autowired
    private UserDao userDao;
    @Autowired
    private UserExtendDao userExtendDao;
    @Autowired
    private UserIntegralService userIntegralService;
    @Autowired
    private UserOrderService userOrderService;
    @Autowired
    private CoinSettingService coinSettingService;
    @Autowired
    private FillMoneySettingDao fillMoneySettingDao;
    @Value("${local.url}")
    private String localUrl;

    /**
     * 微信开发，接收数据，微信扫码支付回调；
     * 其中传输的projectId是包含用户id和订单号；
     * 因为下单支付订单id和此处id可以不一样，所以此处先将需要的信息传输，后面加上随机数，凑够32位即可。
     * 用户id_类型_数据id_金额_随机数;
     * 其中金额的单位是分
     * 数据id没有传输0，类型为1、2、3（1-充值、2-下载、3-组卷）
     * 如：1_1_0_1_1_0123456789ABCDEFGXXX
     *
     * @param request
     * @param response
     */
    @Transactional
    public void wechatScan(HttpServletRequest request, HttpServletResponse response) {
        InputStream inStream = null;
        ByteArrayOutputStream outSteam = null;
        PrintWriter out = null;
        try {
            inStream = request.getInputStream();
            outSteam = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = inStream.read(buffer)) != -1) {
                outSteam.write(buffer, 0, len);
            }
            String notifyData = new String(outSteam.toByteArray(), "utf-8");// 支付结果通知的xml格式数据
            logger.info("接收到的微信扫码消息内容:{}", notifyData);
            WXPay wxpay = new WXPay(new WechatMerchantConfig());
            Map<String, String> notifyMap = WXPayUtil.xmlToMap(notifyData);  // 转换成map
            logger.info("接收到的微信扫码转map消息内容:{}", JsonMapper.nonDefaultMapper().toJson(notifyMap));
            Map<String, String> returnMap = Maps.newHashMap();
            String returnString;
            if (wxpay.isPayResultNotifySignatureValid(notifyMap)) {
                logger.info("微信扫码签名正确");
                // 签名正确
                // 进行处理。
                // 注意特殊情况：（目前没有退款的需求，所以忽略)订单已经退款，但收到了支付结果成功的通知，不应把商户侧订单状态从退款改成支付成功
                //调用微信统一下单接口
                //
                WechatOrderPayReqDto reqDto = new WechatOrderPayReqDto();
                String productId = notifyMap.get("product_id");
                //先查询预付订单是否存在,存在则直接跳过
                UserOrder userOrderByPreNum = userOrderService.findUserOrderByPreNum(productId);
                if(userOrderByPreNum!=null){//已存在,不下单了
                    logger.info("已经存在此订单，返回");
                    returnMap.put("return_code", "FAIL");
                    returnMap.put("return_msg", "已经存在此订单");
                    returnMap.put("appid", notifyMap.get("appid"));
                    returnMap.put("mch_id", notifyMap.get("mch_id"));
                    returnMap.put("nonce_str", notifyMap.get("nonce_str"));
//                returnMap.put("prepay_id", notifyMap.get("product_id"));
                    returnMap.put("result_code", "FAIL");
                    returnMap.put("err_code_des", "已经存在此订单");
                    returnMap.put("sign", notifyMap.get("sign"));
                }else{

                    String[] productIdArr = productId.split("_");
                    //存放用户id
                    reqDto.setAttach(productIdArr[0]);
                    reqDto.setBody("教习网-扫码付款");
                    reqDto.setOpenid(notifyMap.get("openid"));
                    //TODO 付款回调地址
                    reqDto.setNotify_url(localUrl+"/api/anon/wechat/order/callback");
                    reqDto.setTotal_fee(productIdArr.length > 3 && Integer.parseInt(productIdArr[3]) != 0L ? Integer.parseInt(productIdArr[3]) : 1);
                    String orderNum = CodeNumberMaker.getInstance().orderCodeNum();
                    reqDto.setOut_trade_no(orderNum);
                    reqDto.setSpbill_create_ip(NetworkUtil.ipAddress(request));
                    reqDto.setTrade_type("NATIVE");
                    logger.info("下单请求数据:{}", JsonMapper.nonDefaultMapper().toJson(reqDto));
                    DetailDto<Map> detailDto = wechatMerchantService.wechatPayOrder(reqDto);
                    logger.info("下单返回数据:{}", JsonMapper.nonDefaultMapper().toJson(detailDto));
                    Map<String, String> detailMap = detailDto.getDetail();
                    if (detailDto.getStatus()) {//成功,插入数据库数据
                        Long userId = Long.parseLong(reqDto.getAttach());
                        if (userId != null && userId != 0L) {
                            User user = userDao.selectByPrimaryKey(userId);
                            if (user != null) {
                                logger.info("用户存在,入库订单");
                                //插入数据库
                                UserOrder userOrder = new UserOrder();
                                userOrder.setUserId(userId);
                                userOrder.setTime(System.currentTimeMillis());
                                userOrder.setPreNum(productId);
                                userOrder.setOrderNum(reqDto.getOut_trade_no());
                                userOrder.setThirdNum(detailMap.get("prepay_id"));
                                userOrder.setStatus(EntitySetting.COMMON_WAIT);
                                userOrder.setDataId(productIdArr.length > 2 && Long.parseLong(productIdArr[2]) != 0L ? Long.parseLong(productIdArr[2]) : null);
                                userOrder.setType(Integer.parseInt(productIdArr[1]));
                                userOrder.setPayType(EntitySetting.USER_ORDER_PAY_TYPE_WECHAT);
                                userOrder.setPrice(reqDto.getTotal_fee() * 0.01d);
                                userOrder.setSource(EntitySetting.USER_TYPE_PC);
                                if (productIdArr.length > 2 && "1".equals(productIdArr[1])) {
                                    //查询金额可以兑换的备课币
                                    //先查询当前金额在金额配置中是否存在,存在则使用配置中的，不存在则查询通用设置中的
                                    FillMoneySetting fillMoneySetting = fillMoneySettingDao.selectOneByExample(new Example.Builder(FillMoneySetting.class)
                                            .where(WeekendSqls.<FillMoneySetting>custom()
                                                    .andEqualTo(FillMoneySetting::getStatus, EntitySetting.COMMON_SUCCESS)
                                                    .andEqualTo(FillMoneySetting::getPrice, userOrder.getPrice())).build());
                                    if (fillMoneySetting != null) {
                                        userOrder.setCoin(fillMoneySetting.getCoin());
                                    } else {
                                        CoinSetting coinSetting = coinSettingService.findActiveCoinSetting();
                                        if (coinSetting != null) {
                                            userOrder.setCoin(NumberUtil.ceilInt(userOrder.getPrice() / coinSetting.getPrice()));
                                        }
                                    }
                                } else {
                                    CoinSetting coinSetting = coinSettingService.findActiveCoinSetting();
                                    if (coinSetting != null) {
                                        userOrder.setCoin(NumberUtil.ceilInt(userOrder.getPrice() / coinSetting.getPrice()));
                                    }
                                }
                                userOrderService.save(userOrder);
                            }
                        }
                        returnMap.put("return_code", detailMap.get("return_code"));
                        returnMap.put("return_msg", "");
                        returnMap.put("appid", detailMap.get("appid"));
                        returnMap.put("mch_id", detailMap.get("mch_id"));
                        returnMap.put("nonce_str", detailMap.get("nonce_str"));
                        returnMap.put("prepay_id", detailMap.get("prepay_id"));
                        returnMap.put("result_code", detailMap.get("result_code"));
                        returnMap.put("err_code_des", detailMap.get(""));
                        returnMap.put("sign", detailMap.get("sign"));
                    } else {
                        logger.info("微信下单失败");
                        returnMap.put("return_code", "FAIL");
                        returnMap.put("return_msg", "微信下单失败");
                        returnMap.put("appid", detailMap.get("appid"));
                        returnMap.put("mch_id", detailMap.get("mch_id"));
                        returnMap.put("nonce_str", detailMap.get("nonce_str"));
                        returnMap.put("prepay_id", detailMap.get("prepay_id"));
                        returnMap.put("result_code", detailMap.get("result_code"));
                        returnMap.put("err_code_des", detailMap.get("err_code_des"));
                        returnMap.put("sign", detailMap.get("sign"));
                    }
                }
            } else {
                // 签名错误，如果数据里没有sign字段，也认为是签名错误
                logger.info("验证签名失败");
                returnMap.put("return_code", "FAIL");
                returnMap.put("return_msg", "验证签名失败");
                returnMap.put("appid", notifyMap.get("appid"));
                returnMap.put("mch_id", notifyMap.get("mch_id"));
                returnMap.put("nonce_str", notifyMap.get("nonce_str"));
//                returnMap.put("prepay_id", notifyMap.get("product_id"));
                returnMap.put("result_code", "FAIL");
                returnMap.put("err_code_des", "验证签名失败");
                returnMap.put("sign", notifyMap.get("sign"));
            }
            String sign = wechatMerchantService.wechatSign(returnMap).getDetail();
            returnMap.put("sign", sign);
            returnString = WXPayUtil.mapToXml(returnMap);
            logger.info("发送消息内容:{}", returnString);
            // 处理微信信息
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.print(returnString);
            out.flush();
        } catch (Exception e) {
            logger.error("scan-exception:{}", e);
        } finally {
            if (inStream != null) {
                try {
                    inStream.close();
                } catch (IOException e) {
                }
            }
            if (outSteam != null) {
                try {
                    outSteam.close();
                } catch (IOException e) {
                }
            }
            if (out != null) {
                out.close();
            }
        }

    }


    /**
     * 微信开发，微信下单回调
     *
     * @param request
     * @param response
     */
    @Transactional
    public void wechatOrderCallback(HttpServletRequest request, HttpServletResponse response) {
        InputStream inStream = null;
        ByteArrayOutputStream outSteam = null;
        PrintWriter out = null;
        try {
            inStream = request.getInputStream();
            outSteam = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = inStream.read(buffer)) != -1) {
                outSteam.write(buffer, 0, len);
            }
            String notifyData = new String(outSteam.toByteArray(), "utf-8");// 支付结果通知的xml格式数据
            logger.info("接收到的微信扫码回调消息内容:{}", notifyData);
            WXPay wxpay = new WXPay(new WechatMerchantConfig());
            Map<String, String> notifyMap = WXPayUtil.xmlToMap(notifyData);  // 转换成map
            Map<String, String> returnMap = Maps.newHashMap();
            String returnString;
            // TODO cici 根据返回的trade_type判断不同的验证签名方式
            if (wxpay.isResponseSignatureValid(notifyMap)) {
                logger.info("微信扫码回调签名正确");
                // 签名正确
                // 进行处理。
                returnMap.put("return_code", "SUCCESS");
                returnMap.put("return_msg", "OK");
                String orderNum = notifyMap.get("out_trade_no");
                if (!Strings.isNullOrEmpty(orderNum)) {
                    UserOrder userOrder = userOrderService.findByOrderNum(orderNum);
                    if (userOrder != null) {
                        logger.info("更新用户订单");
                        userOrder.setStatus(EntitySetting.COMMON_SUCCESS);
                        userOrder.setPayTime(DateUtil.getInstance().parseDate(notifyMap.get("time_end"), "yyyyMMddHHmmss").getTime());
                        userOrder.setThirdNum(notifyMap.get("transaction_id"));
                        //TODO cici 验证订单金额是否一致
                        userOrderService.save(userOrder);
                        //更新用户的积分、用户积分获取记录
                        UserExtend user = userExtendDao.selectByPrimaryKey(userOrder.getUserId());
                        if (user != null) {
                            logger.info("更新用户记录");
                            user.setCoin(user.getCoin() == null ? userOrder.getCoin() : userOrder.getCoin() + user.getCoin());
                            User u = userDao.selectByPrimaryKey(user.getId());
                            if(u.getFirstPayTime()==null||u.getFirstPayTime()==0L){
                                u.setFirstPayTime(System.currentTimeMillis());
                                userDao.updateByPrimaryKey(u);
                            }
                            userExtendDao.updateByPrimaryKey(user);
                            UserIntegral userIntegral = new UserIntegral();
                            userIntegral.setUserId(user.getId());
                            userIntegral.setOperatorType(EntitySetting.USER_TYPE_PC);
                            userIntegral.setOperatorId(user.getId());
                            userIntegral.setSource(EntitySetting.USER_INTEGRAL_SOURCE_PAYMENT);
                            userIntegral.setNum(userOrder.getCoin());
                            userIntegral.setType(EntitySetting.USER_INTEGRAL_PLUS);
                            userIntegral.setTime(System.currentTimeMillis());
                            userIntegral.setRecordType(EntitySetting.USER_INTEGRAL_RECORD_COIN);
                            userIntegralService.save(userIntegral);
                        }
                    }
                }
            } else {
                // 签名错误，如果数据里没有sign字段，也认为是签名错误
                logger.info("微信扫码回调签名错误");
                returnMap.put("return_code", "FAIL");
                returnMap.put("return_msg", "验证签名失败");
            }
            returnString = WXPayUtil.mapToXml(returnMap);
            logger.info("扫码回调发送消息内容:{}", returnString);
            // 处理微信信息
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.print(returnString);
            out.flush();
        } catch (Exception e) {
            logger.error("scan-callback-exception:{}", e);
        } finally {
            if (inStream != null) {
                try {
                    inStream.close();
                } catch (IOException e) {
                }
            }
            if (outSteam != null) {
                try {
                    outSteam.close();
                } catch (IOException e) {
                }
            }
            if (out != null) {
                out.close();
            }
        }

    }

    /**
     * 支付宝pc支付回调接口；
     * {"gmt_create":"2018-06-21 19:06:18","charset":"UTF-8",
     * "subject":"教习网","sign":"$$$","buyer_id":"2088602052792739",
     * "body":"教习网-支付宝PC支付","invoice_amount":"0.01",
     * "notify_id":"428dea6ce48f41f4390f963e4659af4lmx",
     * "fund_bill_list":"[{\"amount\":\"0.01\",\"fundChannel\":\"ALIPAYACCOUNT\"}]",
     * "notify_type":"trade_status_sync",
     * "trade_status":"TRADE_SUCCESS","receipt_amount":"0.01",
     * "app_id":"2018061360395308","buyer_pay_amount":"0.01",
     * "sign_type":"RSA2","seller_id":"2088131559727734",
     * "gmt_payment":"2018-06-21 19:06:53",
     * "notify_time":"2018-06-21 19:06:53",
     * "passback_params":"{\"userId\":1\"type\":1,\"dataId\":1}","version":"1.0",
     * "out_trade_no":"20180621185945101915891557135257",
     * "total_amount":"0.01","trade_no":"2018062121001004730511572718",
     * "auth_app_id":"2018061360395308","point_amount":"0.00"}
     *
     * @param request 请求
     */
    @Transactional
    public void alipayPcOrderCallback(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> params = Maps.newHashMap();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用。
            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }
        String orderNum = params.get("out_trade_no"); //商家订单编号
        String tradeNo = params.get("trade_no"); //支付宝订单编号
        String tradeStatus = params.get("trade_status"); //回调的交易状态
//        String notifyId = params.get("notify_id"); //回调的交易单号
        String total_amount = params.get("total_amount");//付款金额
        String gmt_create = params.get("gmt_create");//订单创建时间
        String gmt_payment = params.get("gmt_payment");//订单支付时间
        String passback_params = URLDecoder.decode(params.get("passback_params"));//回调回传数据,进行url转码操作
        logger.info("支付宝PC回调获取到的请求数据：{}", JsonMapper.nonDefaultMapper().toJson(params));
        PrintWriter out = null;
        try {
            String returnString = "failure";
            if ("TRADE_FINISHED".equalsIgnoreCase(tradeStatus) || "TRADE_SUCCESS".equalsIgnoreCase(tradeStatus)) {//交易成功
                DetailDto<Map> detailDto = alipayService.alipayPayStatus(params);
                if (detailDto.getStatus()) {//成功
                    returnString = "success";
                    Map<String, String> notifyMap = detailDto.getDetail();
                    logger.info("支付宝PC回调map:{}", JsonMapper.nonDefaultMapper().toJson(notifyMap));
                    UserOrder userOrder = userOrderService.findByOrderNum(orderNum);
                    if (userOrder != null) {
                        logger.info("更新用户订单");
                        userOrder.setStatus(EntitySetting.COMMON_SUCCESS);
                        userOrder.setPayTime(DateUtil.getInstance().parseDate(gmt_payment, "yyyy-MM-dd HH:mm:ss").getTime());
                        userOrder.setThirdNum(tradeNo);
                    } else {//新增订单，因为支付宝预下单的时候没有入库
                        userOrder = new UserOrder();
                        //TODO cici 待从支付的回调信息中获取
                        Map<String, Object> backMap = JsonMapper.nonDefaultMapper().fromJson(passback_params, Map.class);
                        userOrder.setUserId(Long.parseLong(backMap.get("userId").toString()));
                        userOrder.setTime(DateUtil.getInstance().parseDate(gmt_create, "yyyy-MM-dd HH:mm:ss").getTime());
                        userOrder.setPayTime(DateUtil.getInstance().parseDate(gmt_payment, "yyyy-MM-dd HH:mm:ss").getTime());
                        userOrder.setOrderNum(orderNum);
                        userOrder.setThirdNum(tradeNo);
                        userOrder.setPreNum(orderNum);
                        userOrder.setStatus(EntitySetting.COMMON_SUCCESS);
                        userOrder.setDataId(Long.parseLong(backMap.get("dataId").toString()) == 0L ? null : Long.parseLong(backMap.get("dataId").toString()));
                        userOrder.setType(Integer.parseInt(backMap.get("type").toString()));
                        userOrder.setPrice(Double.parseDouble(total_amount));
                        userOrder.setPayType(EntitySetting.USER_ORDER_PAY_TYPE_ALIPAY);
                        userOrder.setSource(EntitySetting.USER_TYPE_PC);
                        if ("1".equals(backMap.get("type").toString())) {
                            //查询金额可以兑换的备课币
                            //先查询当前金额在金额配置中是否存在,存在则使用配置中的，不存在则查询通用设置中的
                            FillMoneySetting fillMoneySetting = fillMoneySettingDao.selectOneByExample(new Example.Builder(FillMoneySetting.class)
                                    .where(WeekendSqls.<FillMoneySetting>custom()
                                            .andEqualTo(FillMoneySetting::getStatus, EntitySetting.COMMON_SUCCESS)
                                            .andEqualTo(FillMoneySetting::getPrice, Double.parseDouble(total_amount))).build());
                            if (fillMoneySetting != null) {
                                userOrder.setCoin(fillMoneySetting.getCoin());
                            } else {
                                CoinSetting coinSetting = coinSettingService.findActiveCoinSetting();
                                if (coinSetting != null) {
                                    userOrder.setCoin(NumberUtil.ceilInt(userOrder.getPrice() / coinSetting.getPrice()));
                                }
                            }
                        } else {
                            CoinSetting coinSetting = coinSettingService.findActiveCoinSetting();
                            if (coinSetting != null) {
                                userOrder.setCoin(NumberUtil.ceilInt(userOrder.getPrice() / coinSetting.getPrice()));
                            }
                        }
                    }
                    //TODO cici 验证订单金额是否一致
                    userOrderService.save(userOrder);
                    //更新用户的积分、用户积分获取记录
                    UserExtend user = userExtendDao.selectByPrimaryKey(userOrder.getUserId());
                    if (user != null) {
                        logger.info("更新用户积分记录-支付宝");
                        user.setCoin(user.getCoin() == null ? userOrder.getCoin() : userOrder.getCoin() + user.getCoin());
                        User u = userDao.selectByPrimaryKey(user.getId());
                        if(u.getFirstPayTime()==null||u.getFirstPayTime()==0L){
                            u.setFirstPayTime(System.currentTimeMillis());
                            userDao.updateByPrimaryKey(u);
                        }
                        userExtendDao.updateByPrimaryKey(user);
                        UserIntegral userIntegral = new UserIntegral();
                        userIntegral.setUserId(user.getId());
                        userIntegral.setOperatorType(EntitySetting.USER_TYPE_PC);
                        userIntegral.setOperatorId(user.getId());
                        userIntegral.setSource(EntitySetting.USER_INTEGRAL_SOURCE_PAYMENT);
                        userIntegral.setNum(userOrder.getCoin());
                        userIntegral.setType(EntitySetting.USER_INTEGRAL_PLUS);
                        userIntegral.setTime(System.currentTimeMillis());
                        userIntegral.setRecordType(EntitySetting.USER_INTEGRAL_RECORD_COIN);
                        userIntegralService.save(userIntegral);
                    }
                }
            }
            // 处理返回信息
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.print(returnString);
            out.flush();
        } catch (Exception e) {
            logger.error("支付宝支付回调Exception:{}", e);
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }
}
