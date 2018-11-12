package com.ixinhoo.papers.controller.base;


import com.chunecai.crumbs.api.client.DetailDto;
import com.chunecai.crumbs.api.client.ListDto;
import com.chunecai.crumbs.api.client.StatusDto;
import com.chunecai.crumbs.api.code.DataTable;
import com.chunecai.crumbs.api.util.DataTableUtil;
import com.ixinhoo.papers.dto.common.CommonIdAndNameDto;
import com.ixinhoo.papers.entity.base.StageSubjects;
import com.ixinhoo.papers.entity.base.Subjects;
import com.ixinhoo.papers.service.base.SubjectsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/api/bg/subjects")
public class SubjectsController {

    @Autowired
    private SubjectsService service;


    @RequestMapping(value = "show/{id}", method = RequestMethod.GET)
    public DetailDto<Subjects> findById(@PathVariable("id") Long id) {
        return new DetailDto<>(true, service.findById(id));
    }


    @RequestMapping(value = "list", method = RequestMethod.POST)
    public DataTable<Subjects> list(HttpServletRequest request) {
        return service.listDatatable(DataTableUtil.toDataTable(request));
    }

    /**
     * 查询所有
     * @return
     */
    @RequestMapping(value = "list-all", method = RequestMethod.GET)
    public ListDto<Subjects> listAll() {
        return service.listAll();
    }

    @RequestMapping(value = "list-stage", method = RequestMethod.POST)
    public DataTable<StageSubjects> listStage(HttpServletRequest request) {
        return service.listStageDatatable(DataTableUtil.toDataTable(request));
    }

    @RequestMapping(value = "sync", method = RequestMethod.GET)
    public StatusDto sync() {
        return service.syncSubjects();
    }


    /**
     * 查询学段下面的学科信息
     *
     * @param stage
     * @return
     */
    @RequestMapping(value = "stage", method = RequestMethod.POST)
    public ListDto<CommonIdAndNameDto> findByStage(Integer stage) {
        return service.findByStage(stage);
    }



}