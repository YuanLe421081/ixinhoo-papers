package com.ixinhoo.papers.controller.website;


import com.chunecai.crumbs.api.client.ListDto;
import com.chunecai.crumbs.api.client.StatusDto;
import com.ixinhoo.papers.entity.website.GradePrivilege;
import com.ixinhoo.papers.service.website.GradePrivilegeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 特权管理
 */
@RestController
@RequestMapping(value = "/api/bg/grade-privilege")
public class GradePrivilegeController {

    @Autowired
    private GradePrivilegeService service;

    /**
     * 查询所有
     *
     * @return
     */
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public ListDto<GradePrivilege> list() {
        return new ListDto<>(true, service.listAll());
    }

    /**
     * 保存所有
     *
     * @param entity
     * @return
     */
    @RequestMapping(value = "save", method = RequestMethod.POST)
    public StatusDto save(@RequestBody List<GradePrivilege> entity) {
        return service.saveAll(entity);
    }
}