package com.ixinhoo.papers.controller.website;


import com.chunecai.crumbs.api.client.ListDto;
import com.chunecai.crumbs.api.client.StatusDto;
import com.ixinhoo.papers.entity.website.GradeSetting;
import com.ixinhoo.papers.entity.website.IntegralRule;
import com.ixinhoo.papers.service.website.GradeSettingService;
import com.ixinhoo.papers.service.website.IntegralRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 积分规则
 */
@RestController
@RequestMapping(value = "/api/bg/integral-rule")
public class IntegralRuleController {

    @Autowired
    private IntegralRuleService service;

    /**
     * 查询所有
     *
     * @return
     */
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public ListDto<IntegralRule> list() {
        return new ListDto<>(true, service.listAll());
    }

    /**
     * 保存所有
     *
     * @param entity
     * @return
     */
    @RequestMapping(value = "save", method = RequestMethod.POST)
    public StatusDto save(@RequestBody List<IntegralRule> entity) {
        return service.saveAll(entity);
    }
}