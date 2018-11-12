package com.ixinhoo.papers.rest.website;


import com.chunecai.crumbs.api.client.ListDto;
import com.ixinhoo.papers.dto.website.BannerSettingDto;
import com.ixinhoo.papers.service.website.BannerSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/anon/banner")
public class BannerSettingRestController {

    @Autowired
    private BannerSettingService service;

    /**
     * banner 列表
     *
     * @param type 类型，1首页banner、2组卷页面
     * @return
     */
    @RequestMapping(value = "list", method = RequestMethod.POST)
    public ListDto<BannerSettingDto> list(Long userId,Integer type) {
        return service.listByStatusAndType(userId,type);
    }

}