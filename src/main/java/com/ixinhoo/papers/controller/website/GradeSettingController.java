package com.ixinhoo.papers.controller.website;


import com.chunecai.crumbs.api.client.ListDto;
import com.chunecai.crumbs.api.client.StatusDto;
import com.ixinhoo.papers.entity.website.GradeSetting;
import com.ixinhoo.papers.service.website.GradeSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 等级配置
 */
@RestController
@RequestMapping(value = "/api/bg/grade-setting")
public class GradeSettingController {

    @Autowired
    private GradeSettingService service;

    /**
     * 查询所有
     *
     * @return
     */
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public ListDto<GradeSetting> list() {
        return new ListDto<>(true, service.listAll());
    }

    /**
     * 保存所有
     *
     * @param entity
     * @return
     */
    @RequestMapping(value = "save", method = RequestMethod.POST)
    public StatusDto save(@RequestBody List<GradeSetting> entity) {
        return service.saveAll(entity);
    }
}