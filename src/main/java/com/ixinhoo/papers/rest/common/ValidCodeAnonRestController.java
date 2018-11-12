package com.ixinhoo.papers.rest.common;


import com.chunecai.crumbs.api.client.DetailDto;
import com.chunecai.crumbs.api.client.StatusDto;
import com.ixinhoo.papers.dto.common.ValidCodeImageDto;
import com.ixinhoo.papers.service.common.ValidCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 验证码--不鉴权
 */
@RestController
@RequestMapping(value = "/api/anon/code")
public class ValidCodeAnonRestController {

    @Autowired
    private ValidCodeService service;


    /**
     * 验证码-返回验证码的base64地址
     *
     * @return
     */
    @RequestMapping(value = "base64", method = RequestMethod.GET)
    public DetailDto<ValidCodeImageDto> url() {
        return service.makeImageCodeData();
    }

    /**
     * 验证码-校验地址
     *
     * @return
     */
    @RequestMapping(value = "valid", method = RequestMethod.POST)
    public StatusDto valid(String data,String code) {
        return service.validImageCodeByData(data,code);
    }


}