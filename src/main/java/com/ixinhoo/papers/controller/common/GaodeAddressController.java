package com.ixinhoo.papers.controller.common;

import com.chunecai.crumbs.api.client.DetailDto;
import com.chunecai.crumbs.code.dto.GaodeAddressDto;
import com.chunecai.crumbs.code.util.location.GaodeAddressUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/bg/gaode")
public class GaodeAddressController {

    /**
     * 逆地址转换
     *
     * @return
     */
    @RequestMapping(value = "decode", method = RequestMethod.POST)
    public DetailDto<GaodeAddressDto> decode(@RequestParam("address") String address) {
        return new DetailDto<>(true, GaodeAddressUtil.getInstance().gaodeAddress(address));
    }

}
