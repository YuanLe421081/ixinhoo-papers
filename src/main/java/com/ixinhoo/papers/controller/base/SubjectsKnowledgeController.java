package com.ixinhoo.papers.controller.base;


import com.chunecai.crumbs.api.client.DetailDto;
import com.chunecai.crumbs.api.client.StatusDto;
import com.chunecai.crumbs.api.code.DataTable;
import com.chunecai.crumbs.api.util.DataTableUtil;
import com.ixinhoo.papers.entity.base.SubjectsKnowledge;
import com.ixinhoo.papers.service.base.SubjectsKnowledgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/api/bg/subjects-knowledge")
public class SubjectsKnowledgeController {

    @Autowired
    private SubjectsKnowledgeService service;


    @RequestMapping(value = "show/{id}", method = RequestMethod.GET)
    public DetailDto<SubjectsKnowledge> findById(@PathVariable("id") Long id) {
        return new DetailDto<>(true, service.findById(id));
    }


    @RequestMapping(value = "list", method = RequestMethod.POST)
    public DataTable<SubjectsKnowledge> list(HttpServletRequest request) {
        return service.listDatatable(DataTableUtil.toDataTable(request));
    }


    @RequestMapping(value = "sync", method = RequestMethod.GET)
    public StatusDto sync() {
        return service.syncSubjectsKnowledge();
    }


}