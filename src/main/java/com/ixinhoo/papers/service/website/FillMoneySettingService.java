package com.ixinhoo.papers.service.website;

import com.chunecai.crumbs.alipay.dto.AlipayPcOrderPayReqDto;
import com.chunecai.crumbs.alipay.service.AlipayService;
import com.chunecai.crumbs.api.client.DetailDto;
import com.chunecai.crumbs.api.client.ListDto;
import com.chunecai.crumbs.api.dao.BaseDao;
import com.chunecai.crumbs.api.service.BaseService;
import com.chunecai.crumbs.code.util.image.QrCodeZxingUtil;
import com.chunecai.crumbs.code.util.mapper.BeanMapper;
import com.chunecai.crumbs.code.util.number.CodeNumberMaker;
import com.chunecai.crumbs.wechat.service.WechatMerchantService;
import com.google.common.collect.Lists;
import com.google.zxing.WriterException;
import com.ixinhoo.papers.dao.website.FillMoneySettingDao;
import com.ixinhoo.papers.dto.website.FillMoneyScanPayDto;
import com.ixinhoo.papers.entity.EntitySetting;
import com.ixinhoo.papers.entity.website.FillMoneySetting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.weekend.WeekendSqls;

import java.io.IOException;
import java.util.List;

@Service
public class FillMoneySettingService extends BaseService<FillMoneySetting> {
    @Autowired
    private FillMoneySettingDao dao;
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

    /**
     * 根据用户的主键id查询扫码支付的相关信息
     *
     * @param userId
     * @return
     */
    public ListDto<FillMoneyScanPayDto> scanPayQrcodeByUserId(Long userId) {
        ListDto<FillMoneyScanPayDto> dto = new ListDto<>(false);
        if (userId == null || userId == 0L) {
            dto.setMsg("用户不存在");
        } else {
            List<FillMoneySetting> list = dao.selectByExample(new Example.Builder(FillMoneySetting.class)
                    .where(WeekendSqls.<FillMoneySetting>custom()
                            .andEqualTo(FillMoneySetting::getStatus, EntitySetting.COMMON_SUCCESS)).build());
            List<FillMoneyScanPayDto> listDto = Lists.newArrayList();
            list.forEach(d -> {
                FillMoneyScanPayDto temp = BeanMapper.map(d, FillMoneyScanPayDto.class);
                //用户id_类型_数据id_金额_随机数;
                String wechatProductId = userId + "_1_" + d.getId()  +"_"+ (int)(d.getPrice()*100)+ "_";
                int wechatLength = wechatProductId.length();
                wechatProductId = wechatProductId + CodeNumberMaker.getInstance().orderCodeNum(32 - wechatLength);
                temp.setWechatNum(wechatProductId);
                DetailDto<String> wechatDto = wechatMerchantService.wechatUrlSignByProudctId(wechatProductId);
                if (wechatDto.getStatus()) {
                    temp.setWechatUrl(wechatDto.getDetail());
                    try {
                        temp.setWechatQrcode(QrCodeZxingUtil.getInstall().readQrCodeBase64(wechatDto.getDetail()));
                    } catch (WriterException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                //支付宝
                AlipayPcOrderPayReqDto alipayReq = new AlipayPcOrderPayReqDto();
                alipayReq.setMethod("alipay.trade.page.pay");
                alipayReq.setNotify_url(localUrl+"/api/anon/alipay/order/callback");
                alipayReq.setBody("教习网-支付宝PC支付");
                alipayReq.setSubject("教习网");
                alipayReq.setOut_trade_no(CodeNumberMaker.getInstance().orderCodeNum(32));
                alipayReq.setTotal_amount(d.getPrice());//单位是元
                //支付宝回传参数
                alipayReq.setPassback_params("{\"userId\":" + userId + ",\"type\":1,\"dataId\":" + d.getId() + "}");
                alipayReq.setTimeout_express("1d");//1天失效
                alipayReq.setQr_pay_mode("4");
                alipayReq.setQrcode_width(125l);
                DetailDto<String> alipayDto = alipayService.alipayPcPayOrder(alipayReq);
                temp.setAlipayNum(alipayReq.getOut_trade_no());
                if (alipayDto.getStatus()) {
                    //TODO cici注意此处返回的是html页面文本，前端需要进行插入处理
                    temp.setAlipayQrcode(alipayDto.getDetail());
                }
                listDto.add(temp);
            });
            dto.setList(listDto);
            dto.setStatus(true);
        }
        return dto;
    }
}