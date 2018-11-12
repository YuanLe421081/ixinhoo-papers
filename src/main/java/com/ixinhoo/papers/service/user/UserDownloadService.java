package com.ixinhoo.papers.service.user;

import com.chunecai.crumbs.alipay.dto.AlipayPcOrderPayReqDto;
import com.chunecai.crumbs.alipay.service.AlipayService;
import com.chunecai.crumbs.api.client.DetailDto;
import com.chunecai.crumbs.api.client.ListDto;
import com.chunecai.crumbs.api.client.PageDto;
import com.chunecai.crumbs.api.dao.BaseDao;
import com.chunecai.crumbs.api.service.BaseService;
import com.chunecai.crumbs.api.shiro.ShiroUser;
import com.chunecai.crumbs.api.util.UsersUtil;
import com.chunecai.crumbs.code.util.collection.Collections3;
import com.chunecai.crumbs.code.util.image.QrCodeZxingUtil;
import com.chunecai.crumbs.code.util.mapper.BeanMapper;
import com.chunecai.crumbs.code.util.number.CodeNumberMaker;
import com.chunecai.crumbs.wechat.service.WechatMerchantService;
import com.cnjy21.api.constant.APIConstant;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.zxing.WriterException;
import com.ixinhoo.papers.dao.resources.*;
import com.ixinhoo.papers.dao.user.UserDao;
import com.ixinhoo.papers.dao.user.UserDownloadDao;
import com.ixinhoo.papers.dao.user.UserExtendDao;
import com.ixinhoo.papers.dao.user.UserIntegralDao;
import com.ixinhoo.papers.dao.website.CoinSettingDao;
import com.ixinhoo.papers.dto.user.DocumentScanPayDto;
import com.ixinhoo.papers.dto.user.UserDownloadDto;
import com.ixinhoo.papers.entity.EntitySetting;
import com.ixinhoo.papers.entity.resources.*;
import com.ixinhoo.papers.entity.user.*;
import com.ixinhoo.papers.entity.website.CoinSetting;
import com.ixinhoo.papers.service.file.QiniuFileService;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.weekend.WeekendSqls;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserDownloadService extends BaseService<UserDownload> {
    @Autowired
    private UserDownloadDao dao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private UserExtendDao userExtendDao;
    @Autowired
    private DocumentDao documentDao;
    @Autowired
    private DocumentExtendDao documentExtendDao;
    @Autowired
    private DocumentStage1Dao documentStage1Dao;
    @Autowired
    private DocumentStage2Dao documentStage2Dao;
    @Autowired
    private DocumentStage3Dao documentStage3Dao;
    @Autowired
    private QiniuFileService fileService;
    @Autowired
    private UserIntegralDao userIntegralDao;
    @Autowired
    private CoinSettingDao coinSettingDao;
    @Autowired
    private UserOrderService userOrderService;
    @Autowired
    private WechatMerchantService wechatMerchantService;
    @Autowired
    private AlipayService alipayService;
    @Value("${local.url}")
    private String localUrl;

    @Override
    protected BaseDao getBaseDao() {
        return dao;
    }

    @Transactional
    public ListDto<String> createByUserAndDocument(Long userId, List<Long> documentIds) {
        ListDto<String> dto = new ListDto<>(false);
        if (userId == null || userId == 0L) {
            dto.setMsg("用户未传输");
        } else if (Collections3.isEmpty(documentIds)) {
            dto.setMsg("文档未传输");
        } else {
            UserExtend userExtend = userExtendDao.selectByPrimaryKey(userId);
            if (userExtend == null) {
                dto.setMsg("用户不存在");
            } else {
                List<Document> documents = documentDao.selectByExample(new Example.Builder(Document.class)
                        .where(WeekendSqls.<Document>custom().andEqualTo(Document::getStatus, EntitySetting.COMMON_SUCCESS)
                                .andIn(Document::getId, documentIds))
                        .build());
                //统计文档所需的备课币
                Integer coin = 0;
                for (Document d : documents) {
                    coin += (d.getCoin() == null ? 0 : d.getCoin());
                }
                //判断用户的备课币
                Integer userCoin = userExtend.getCoin() == null ? 0 : userExtend.getCoin();
                if (coin > userCoin) {
                    dto.setMsg("用户备课币不足");
                } else {
                    dto.setStatus(true);
                    User mobileUser = userDao.selectByPrimaryKey(userId);
                    ShiroUser user = UsersUtil.ShiroUser();
                    List<String> list = Lists.newArrayList();
                    List<UserDownload> userDownloadList = Lists.newArrayList();
                    List<UserIntegral> userIntegralList = Lists.newArrayList();
                    //查询资源的下载地址
                    documents.forEach(d -> {
                        DocumentStage documentStage = null;
                        if (APIConstant.STAGE_XIAOXUE == d.getStage()) {
                            DocumentStage1 temp = new DocumentStage1();
                            temp.setDocumentId(d.getId());
                            documentStage = BeanMapper.map(documentStage1Dao.selectOne(temp), DocumentStage.class);
                        } else if (APIConstant.STAGE_CHUZHONG == d.getStage()) {
                            DocumentStage2 temp = new DocumentStage2();
                            temp.setDocumentId(d.getId());
                            documentStage = BeanMapper.map(documentStage2Dao.selectOne(temp), DocumentStage.class);
                        } else if (APIConstant.STAGE_GAOZHONG == d.getStage()) {
                            DocumentStage3 temp = new DocumentStage3();
                            temp.setDocumentId(d.getId());
                            documentStage = BeanMapper.map(documentStage3Dao.selectOne(temp), DocumentStage.class);
                        }
                        if (documentStage != null && !Strings.isNullOrEmpty(documentStage.getDownloadAddress())) {
                            DetailDto<String> temp = fileService.getPrivateUrlByKey(documentStage.getDownloadAddress());
                            if (temp.getStatus()) {
                                list.add(temp.getDetail());
                                UserDownload userDownload = new UserDownload();
                                UserIntegral userIntegral = new UserIntegral();
                                userDownload.setUserId(userId);
                                userDownload.setTime(System.currentTimeMillis());
                                userDownload.setType(d.getTypeId());
                                userDownload.setDataId(d.getId());
                                userDownload.setVersionId(d.getVersionId());
                                userDownload.setVersionName(d.getVersionName());
                                if (user != null) {
                                    userDownload.setCreateId(user.getId());
                                    userDownload.setUpdateId(user.getId());
                                    userIntegral.setCreateId(user.getId());
                                    userIntegral.setUpdateId(user.getId());
                                }
                                userDownload.setCreateTime(System.currentTimeMillis());
                                userDownload.setUpdateTime(System.currentTimeMillis());
                                userIntegral.setCreateTime(System.currentTimeMillis());
                                userIntegral.setUpdateTime(System.currentTimeMillis());
                                userIntegral.setType(d.getTypeId());
                                userIntegral.setRecordType(EntitySetting.USER_INTEGRAL_RECORD_COIN);
                                userIntegral.setOperatorType(EntitySetting.USER_INTEGRAL_OPERATOR_USER);
                                userIntegral.setOperatorId(userId);
                                userIntegral.setUserId(userId);
                                userIntegral.setNum(d.getCoin());
                                userIntegral.setSource(EntitySetting.USER_INTEGRAL_SOURCE_CONSUME);
                                userIntegral.setTime(System.currentTimeMillis());
                                if (d.getCoin() != null && d.getCoin() != 0L) {
                                    userIntegralList.add(userIntegral);
                                    //更新用户订单记录--有付费的才算
                                    UserOrder userOrder = new UserOrder();
                                    userOrder.setTime(System.currentTimeMillis());
                                    userOrder.setStatus(EntitySetting.COMMON_SUCCESS);
                                    userOrder.setUserId(userId);
                                    userOrder.setUserName(mobileUser.getName());
                                    userOrder.setCoin(userIntegral.getNum());
                                    userOrder.setDataId(d.getId());
                                    userOrder.setOrderNum(CodeNumberMaker.getInstance().orderCodeNum());
                                    userOrder.setSource(EntitySetting.USER_TYPE_PC);
                                    userOrder.setPayType(EntitySetting.USER_ORDER_PAY_TYPE_COIN);
                                    userOrder.setType(EntitySetting.USER_ORDER_TYPE_DOWNLOAD);
                                    userOrderService.save(userOrder);
                                }
                                userDownloadList.add(userDownload);
                            }
                        }
                    });
                    //更新用户备课币数量和备课币记录
                    dto.setList(list);
                    if (Collections3.isNotEmpty(userDownloadList)) {
                        dao.insertList(userDownloadList);
                    }
                    if (Collections3.isNotEmpty(userIntegralList)) {
                        userIntegralDao.insertList(userIntegralList);
                    }
                    userExtend.setCoin(userCoin - coin);
                    if (user != null) {
                        userExtend.setUpdateId(user.getId());
                    }
                    userExtend.setUpdateTime(System.currentTimeMillis());
                    userExtendDao.updateByPrimaryKey(userExtend);
                }
            }
        }
        return dto;
    }


    public PageDto<UserDownloadDto> listDocumentByUserDownload(Long userId, Integer p, Integer s) {
        PageDto<UserDownloadDto> dto = new PageDto<>(false);
        if (userId == null || userId == 0L) {
            dto.setMsg("用户为空");
        } else {
            dto.setStatus(true);
            if (p == null) {
                p = 0;
            }
            if (s == null) {
                s = 10;
            }
            List<UserDownload> downloads = dao.selectByExampleAndRowBounds(new Example.Builder(UserDownload.class)
                    .where(WeekendSqls.<UserDownload>custom().andEqualTo(UserDownload::getUserId, userId))
                    .build(), new RowBounds(p, s));
            int count = dao.selectCountByExample(new Example.Builder(UserDownload.class)
                    .where(WeekendSqls.<UserDownload>custom().andEqualTo(UserDownload::getUserId, userId))
                    .build());
            dto.setCountPage(count / s);
            dto.setCount(count);
            dto.setSize(s);
            dto.setPage(p);
            List<Long> documentIds = downloads.stream().map(d -> d.getDataId()).collect(Collectors.toList());
            dto.setStatus(true);
            if (Collections3.isNotEmpty(documentIds)) {
                Map<Long, UserDownload> map = Maps.newHashMap();
                downloads.forEach(d -> {
                    map.put(d.getDataId(), d);
                });
                List<Document> documents = documentDao.selectByExample(new Example.Builder(Document.class)
                        .where(WeekendSqls.<Document>custom()
                                .andIn(Document::getId, documentIds))
                        .build());
                //查询用户的评分信息
                List<DocumentExtend01> documentExtends = documentExtendDao.selectByExample(new Example.Builder(DocumentExtend01.class)
                        .where(WeekendSqls.<DocumentExtend01>custom()
                                .andIn(DocumentExtend01::getId, documentIds))
                        .build());
                Map<Long, DocumentExtend01> extendMap = Maps.newHashMap();
                documentExtends.forEach(d -> {
                    extendMap.put(d.getId(), d);
                });
                List<UserDownloadDto> list = Lists.newArrayList();
                Map<Long,Document> documentMap = Maps.newHashMap();
                documents.forEach(d -> {
                    documentMap.put(d.getId(),d);
                });
                downloads.forEach(d2->{
                    if(documentMap.containsKey(d2.getDataId())){
                        Document d = documentMap.get(d2.getDataId());
                        if(map.containsKey(d.getId())) {
                            UserDownload userDownload = map.get(d.getId());
                            UserDownloadDto temp = BeanMapper.map(d, UserDownloadDto.class);
                            temp.setDownloadId(userDownload.getId());
                            temp.setTime(userDownload.getTime());
                            if (extendMap.containsKey(d.getId())) {
                                DocumentExtend01 extend = extendMap.get(d.getId());
                                temp.setScore(extend.getScore());
                                temp.setScoreNum(extend.getScoreNum());
                            }
                            list.add(temp);
                        }
                    }
                });
                dto.setList(list);
            }
        }
        return dto;
    }

    @Transactional
    public DetailDto<DocumentScanPayDto> checkDownloadResource(Long userId, Long id) {
        DetailDto<DocumentScanPayDto> dto = new DetailDto<>(false);
        if (userId == null || userId == 0L) {
            dto.setMsg("用户为空");
        } else if (id == null || id == 0L) {
            dto.setMsg("组卷id为空");
        } else {
            UserExtend user = userExtendDao.selectByPrimaryKey(userId);
            if (user == null) {
                dto.setMsg("用户不存在");
            } else {
                Document document = documentDao.selectByPrimaryKey(id);
                if (document == null) {
                    dto.setMsg("文档不存在");
                } else {
                    DocumentScanPayDto documentScanPayDto = new DocumentScanPayDto();
                    Integer coin = document.getCoin();
                    if (coin == null || coin == 0L) {
                        dto.setStatus(true);
                    } else if (user.getCoin() != null && user.getCoin() >= coin) {
                        dto.setStatus(true);
                    } else {
                        CoinSetting coinSetting = new CoinSetting();
                        coinSetting.setStatus(EntitySetting.COMMON_SUCCESS);
                        List<CoinSetting> list = coinSettingDao.select(coinSetting);
                        if (Collections3.isEmpty(list)) {//没有设置,默认可以直接下载
                            dto.setStatus(true);
                        } else {
                            dto.setStatus(false);
                            coinSetting = list.get(0);
                            documentScanPayDto.setUserCoin(user.getCoin());
                            documentScanPayDto.setDownloadIcon(coin);
                            Integer minCoin = coin-(user.getCoin()==null?0:user.getCoin());
                            Double price = coinSetting.getPrice() * minCoin;
                            //用户id_类型_数据id_金额_随机数;
                            String wechatProductId = userId + "_2_" + id + "_" +  (int)(price*100) + "_";
                            int wechatLength = wechatProductId.length();
                            wechatProductId = wechatProductId + CodeNumberMaker.getInstance().orderCodeNum(32 - wechatLength);
                            DetailDto<String> wechatDto = wechatMerchantService.wechatUrlSignByProudctId(wechatProductId);
                            if (wechatDto.getStatus()) {
                                try {
                                    documentScanPayDto.setWechatQrcode(QrCodeZxingUtil.getInstall().readQrCodeBase64(wechatDto.getDetail()));
                                    documentScanPayDto.setWechatNum(wechatProductId);
                                } catch (WriterException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            //支付宝
                            AlipayPcOrderPayReqDto alipayReq = new AlipayPcOrderPayReqDto();
                            alipayReq.setMethod("alipay.trade.page.pay");
                            alipayReq.setNotify_url(localUrl + "/api/anon/alipay/order/callback");
                            alipayReq.setBody("教习网-支付宝PC支付");
                            alipayReq.setSubject("教习网");
                            alipayReq.setOut_trade_no(CodeNumberMaker.getInstance().orderCodeNum(32));
                            alipayReq.setTotal_amount(coinSetting.getPrice()*minCoin);//单位是元
                            //支付宝回传参数
                            alipayReq.setPassback_params("{\"userId\":" + userId + ",\"type\":2,\"dataId\":" + id + "}");
                            alipayReq.setTimeout_express("1d");//1天失效
                            alipayReq.setQr_pay_mode("4");
                            alipayReq.setQrcode_width(125l);
                            DetailDto<String> alipayDto = alipayService.alipayPcPayOrder(alipayReq);
                            documentScanPayDto.setAlipayNum(alipayReq.getOut_trade_no());
                            if (alipayDto.getStatus()) {
                                //TODO cici注意此处返回的是html页面文本，前端需要进行插入处理
                                documentScanPayDto.setAlipayQrcode(alipayDto.getDetail());
                            }
                        }
                    }
                    if (dto.getStatus()) {
                        DocumentExtend01 extend = documentExtendDao.selectByPrimaryKey(id);
                        if(extend==null){
                            extend = new DocumentExtend01();
                            extend.setDownloadNum(1L);
                            documentExtendDao.insert(extend);
                        }else{
                            extend.setDownloadNum(extend.getDownloadNum()==null?1L:extend.getDownloadNum()+1L);
                            documentExtendDao.updateByPrimaryKey(extend);
                        }
                        //获取资源地址
                        DocumentStage documentStage = null;
                        if (APIConstant.STAGE_XIAOXUE == document.getStage()) {
                            DocumentStage1 temp = new DocumentStage1();
                            temp.setDocumentId(document.getId());
                            documentStage = BeanMapper.map(documentStage1Dao.selectOne(temp), DocumentStage.class);
                        } else if (APIConstant.STAGE_CHUZHONG == document.getStage()) {
                            DocumentStage2 temp = new DocumentStage2();
                            temp.setDocumentId(document.getId());
                            documentStage = BeanMapper.map(documentStage2Dao.selectOne(temp), DocumentStage.class);
                        } else if (APIConstant.STAGE_GAOZHONG == document.getStage()) {
                            DocumentStage3 temp = new DocumentStage3();
                            temp.setDocumentId(document.getId());
                            documentStage = BeanMapper.map(documentStage3Dao.selectOne(temp), DocumentStage.class);
                        }
                        if (documentStage != null && !Strings.isNullOrEmpty(documentStage.getDownloadAddress())) {
                            DetailDto<String> temp = fileService.getPrivateUrlByKey(documentStage.getDownloadAddress());
                            if (temp.getStatus()) {
                                documentScanPayDto.setDownloadUrl(temp.getDetail());
                                UserDownload userDownload = new UserDownload();
                                UserIntegral userIntegral = new UserIntegral();
                                userDownload.setUserId(userId);
                                userDownload.setTime(System.currentTimeMillis());
                                userDownload.setType(document.getTypeId());
                                userDownload.setDataId(document.getId());
                                userDownload.setVersionId(document.getVersionId());
                                userDownload.setVersionName(document.getVersionName());
                                if (user != null) {
                                    userDownload.setCreateId(user.getId());
                                    userDownload.setUpdateId(user.getId());
                                    userIntegral.setCreateId(user.getId());
                                    userIntegral.setUpdateId(user.getId());
                                }
                                userDownload.setCreateTime(System.currentTimeMillis());
                                userDownload.setUpdateTime(System.currentTimeMillis());
                                userIntegral.setCreateTime(System.currentTimeMillis());
                                userIntegral.setUpdateTime(System.currentTimeMillis());
                                userIntegral.setType(document.getTypeId());
                                userIntegral.setRecordType(EntitySetting.USER_INTEGRAL_RECORD_COIN);
                                userIntegral.setOperatorType(EntitySetting.USER_INTEGRAL_OPERATOR_USER);
                                userIntegral.setOperatorId(userId);
                                userIntegral.setUserId(userId);
                                userIntegral.setNum(document.getCoin());
                                userIntegral.setSource(EntitySetting.USER_INTEGRAL_SOURCE_CONSUME);
                                userIntegral.setTime(System.currentTimeMillis());
                                if (document.getCoin() != null && document.getCoin() != 0L) {
                                    //更新用户订单记录--有付费的才算
                                    UserOrder userOrder = new UserOrder();
                                    userOrder.setTime(System.currentTimeMillis());
                                    userOrder.setStatus(EntitySetting.COMMON_SUCCESS);
                                    userOrder.setUserId(userId);
                                    User mobileUser = userDao.selectByPrimaryKey(user.getId());
                                    userOrder.setUserName(mobileUser.getName());
                                    userOrder.setCoin(userIntegral.getNum());
                                    userOrder.setDataId(document.getId());
                                    userOrder.setOrderNum(CodeNumberMaker.getInstance().orderCodeNum());
                                    userOrder.setSource(EntitySetting.USER_TYPE_PC);
                                    userOrder.setPayType(EntitySetting.USER_ORDER_PAY_TYPE_COIN);
                                    userOrder.setType(EntitySetting.USER_ORDER_TYPE_DOWNLOAD);
                                    userOrderService.save(userOrder);
                                    //更新用户的备课币数量
                                    user.setCoin(user.getCoin() - document.getCoin());
                                    user.setUpdateId(user.getId());
                                    user.setUpdateTime(System.currentTimeMillis());
                                    userExtendDao.updateByPrimaryKey(user);
                                }
                                dao.insert(userDownload);
                            }
                        }
                    }
                    dto.setDetail(documentScanPayDto);
                }
            }
        }
        return dto;
    }
}