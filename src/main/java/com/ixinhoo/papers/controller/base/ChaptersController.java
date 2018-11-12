package com.ixinhoo.papers.controller.base;


import com.chunecai.crumbs.api.client.DetailDto;
import com.chunecai.crumbs.api.client.StatusDto;
import com.chunecai.crumbs.api.code.DataTable;
import com.chunecai.crumbs.api.code.JsTree;
import com.chunecai.crumbs.api.code.JsTreeAjax;
import com.chunecai.crumbs.api.util.DataTableUtil;
import com.ixinhoo.papers.dto.common.CommonIdAndNameDto;
import com.ixinhoo.papers.entity.base.Chapters;
import com.ixinhoo.papers.service.base.ChaptersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(value = "/api/bg/chapters")
public class ChaptersController {

    @Autowired
    private ChaptersService service;


    @RequestMapping(value = "show/{id}", method = RequestMethod.GET)
    public DetailDto<Chapters> findById(@PathVariable("id") Long id) {
        return new DetailDto<>(true, service.findById(id));
    }


    @RequestMapping(value = "list", method = RequestMethod.POST)
    public DataTable<Chapters> list(HttpServletRequest request) {
        return service.listDatatable(DataTableUtil.toDataTable(request));
    }


    /**
     * 根据学段、学科、父级节点查询章节信息
     *
     * @return
     */
    @RequestMapping(value = "chapter", method = RequestMethod.GET)
    public List<JsTreeAjax> chapter(Integer stage, Long subjectId, Long supId) {
        return service.findChaptersByStageAndSubjectAndSupId(stage, subjectId, supId);
    }

    @RequestMapping(value = "sync-version", method = RequestMethod.GET)
    public StatusDto syncVersion() {
        return service.syncVersion();
    }


    @RequestMapping(value = "sync-book", method = RequestMethod.GET)
    public StatusDto syncBook() {
        return service.syncBook();
    }


    @RequestMapping(value = "sync-chapters", method = RequestMethod.GET)
    public StatusDto syncChapters() {
        return service.syncChapters();
    }


    @RequestMapping(value = "sync", method = RequestMethod.GET)
    public StatusDto sync() {
        return service.syncAllChapters();
    }


}