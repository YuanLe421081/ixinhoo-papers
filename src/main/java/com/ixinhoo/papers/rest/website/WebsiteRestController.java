package com.ixinhoo.papers.rest.website;


import com.chunecai.crumbs.api.client.DetailDto;
import com.chunecai.crumbs.api.client.ListDto;
import com.ixinhoo.papers.dto.website.HomeRecommendDto;
import com.ixinhoo.papers.dto.website.HomeWebsiteDto;
import com.ixinhoo.papers.service.website.WebsiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 统计数据&精品推荐
 */
@RestController
@RequestMapping(value = "/api/anon/website")
public class WebsiteRestController {

    @Autowired
    private WebsiteService service;

    /**
     * 首页统计
     *
     * @return
     */
    @RequestMapping(value = "count", method = RequestMethod.GET)
    public DetailDto<HomeWebsiteDto> count() {
        return service.homeWebsite();
    }

    /**
     * 首页-精品推荐
     *
     * @return
     */
    @RequestMapping(value = "recommend", method = RequestMethod.POST)
    public ListDto<HomeRecommendDto> recommend(Long userId) {
        return service.homeRecommend(userId);
    }


}