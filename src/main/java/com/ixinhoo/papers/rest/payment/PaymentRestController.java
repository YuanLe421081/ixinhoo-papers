package com.ixinhoo.papers.rest.payment;


import com.ixinhoo.papers.service.payment.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 微信商户相关&支付宝PC 支付相关的rest；
 * 包含微信扫码支付（微信扫码支付前提是需要开通微信公众号才可以用）；
 *
 * @author cici
 */
@RestController
@RequestMapping(value = "/api/anon")
public class PaymentRestController {
    private Logger logger = LoggerFactory.getLogger(PaymentRestController.class);
    @Autowired
    private PaymentService service;

    /**
     * 微信开发，接收数据，微信扫码支付回调；
     * 其中传输的projectId是包含用户id和订单号；
     * 因为下单支付订单id和此处id可以不一样，所以此处先将需要的信息传输，后面加上随机数，凑够32位即可。
     * 用户id_类型_数据id_金额_随机数;
     * 其中金额的单位是分
     * 数据id没有传输0，类型为1、2、3（1-充值、2-下载、3-组卷）
     * 如：1_1_0_1_0123456789ABCDEFGXXX
     * @param request
     * @param response
     */
    @RequestMapping(value = "/wechat/merchant/scan")
    public void wechatScan(HttpServletRequest request, HttpServletResponse response) {
        logger.info("scan--callback");
        service.wechatScan(request, response);
        logger.info("scan--callback--end");
    }

    /**
     * 微信开发，微信下单回调
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "/wechat/order/callback")
    public void wechatOrderCallback(HttpServletRequest request, HttpServletResponse response) {
        logger.info("order--callback");
        service.wechatOrderCallback(request, response);
        logger.info("order--callback--end");
    }

    /**
     * 支付宝支付回调接口
     *
     * @param request 请求
     */
    @RequestMapping(value = "/alipay/order/callback")
    public void alipayPcOrderPayCallBack(HttpServletRequest request, HttpServletResponse response) {
        logger.info("order--callback");
        service.alipayPcOrderCallback(request, response);
        logger.info("order--callback--end");
    }


}