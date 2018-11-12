package com.ixinhoo.papers.controller.papers;


import com.chunecai.crumbs.api.client.DetailDto;
import com.chunecai.crumbs.api.client.StatusDto;
import com.chunecai.crumbs.api.code.DataTable;
import com.chunecai.crumbs.api.util.DataTableUtil;
import com.ixinhoo.papers.entity.papers.QuestionType;
import com.ixinhoo.papers.entity.papers.SubjectQuestionType;
import com.ixinhoo.papers.service.papers.QuestionTypeService;
import com.ixinhoo.papers.service.papers.SubjectQuestionTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/api/bg/question-type")
public class QuestionTypeController {

    @Autowired
    private QuestionTypeService service;

    @Autowired
    private SubjectQuestionTypeService subjectQuestionTypeService;


    @RequestMapping(value = "show/{id}", method = RequestMethod.GET)
    public DetailDto<QuestionType> findById(@PathVariable("id") Long id) {
        return new DetailDto<>(true, service.findById(id));
    }


    @RequestMapping(value = "list", method = RequestMethod.POST)
    public DataTable<QuestionType> list(HttpServletRequest request) {
        return service.listDatatable(DataTableUtil.toDataTable(request));
    }

    @RequestMapping(value = "list-subject", method = RequestMethod.POST)
    public DataTable<SubjectQuestionType> listSubjectQuestionType(HttpServletRequest request) {
        return subjectQuestionTypeService.listDatatable(DataTableUtil.toDataTable(request));
    }

    @RequestMapping(value = "sync", method = RequestMethod.GET)
    public StatusDto sync() {
        return service.syncQuestionType();
    }


}