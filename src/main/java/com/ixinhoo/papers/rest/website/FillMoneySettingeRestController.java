package com.ixinhoo.papers.rest.website;


import com.chunecai.crumbs.api.client.ListDto;
import com.ixinhoo.papers.dto.website.FillMoneyScanPayDto;
import com.ixinhoo.papers.service.website.FillMoneySettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 充值中心--在线充值
 */
@RestController
@RequestMapping(value = "/api/v1/fill-money")
public class FillMoneySettingeRestController {

    @Autowired
    private FillMoneySettingService service;


    /**
     * 充值中心--在线充值;
     * 根据用户id查询组装扫描的二维码信息
     *
     * @return
     */
    @RequestMapping(value = "pay", method = RequestMethod.POST)
    public ListDto<FillMoneyScanPayDto> scanPay(Long userId) {
        return service.scanPayQrcodeByUserId(userId);
    }


}