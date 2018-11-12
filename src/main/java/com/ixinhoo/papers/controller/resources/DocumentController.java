package com.ixinhoo.papers.controller.resources;


import com.chunecai.crumbs.api.client.DetailDto;
import com.chunecai.crumbs.api.client.StatusDto;
import com.chunecai.crumbs.api.code.DataTable;
import com.chunecai.crumbs.api.util.DataTableUtil;
import com.ixinhoo.papers.dto.resources.DocumentUploadDto;
import com.ixinhoo.papers.entity.resources.Document;
import com.ixinhoo.papers.service.resources.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(value = "/api/bg/document")
public class DocumentController {

    @Autowired
    private DocumentService service;


    @RequestMapping(value = "save", method = RequestMethod.POST)
    public StatusDto save(DocumentUploadDto entity) {
        return service.saveDocument(entity);
    }

    @RequestMapping(value = "show/{id}", method = RequestMethod.GET)
    public DetailDto<Document> findById(@PathVariable("id") Long id) {
        return new DetailDto<>(true, service.findById(id));
    }


    @RequestMapping(value = "list", method = RequestMethod.POST)
    public DataTable<Document> list(HttpServletRequest request) {
        return service.listDatatable(DataTableUtil.toDataTable(request));
    }

    @RequestMapping(value = "audit", method = RequestMethod.POST)
    public StatusDto updateAuditStatusByIds(@RequestParam List<Long> ids, Integer status) {
        return service.updateAuditStatusByIdsAndStatus(ids, status);
    }

    /**
     * 同步小学
     *
     * @return
     */
    @RequestMapping(value = "sync-stage1", method = RequestMethod.GET)
    public StatusDto syncDocumentStage1() {
//        return service.syncDocumentStage1();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                service.syncDocumentStage1();
            }
        });
        t.start();
        return new StatusDto(true);
    }

    @RequestMapping(value = "sync-stage2", method = RequestMethod.GET)
    public StatusDto syncDocumentStage2() {
//        return service.syncDocumentStage2();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                service.syncDocumentStage2();
            }
        });
        t.start();
        return new StatusDto(true);
    }

    @RequestMapping(value = "sync-stage3", method = RequestMethod.GET)
    public StatusDto syncDocumentStage3() {
//        return service.syncDocumentStage3();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                service.syncDocumentStage3();
            }
        });
        t.start();
        return new StatusDto(true);
    }

    @RequestMapping(value = "sync", method = RequestMethod.GET)
    public StatusDto sync() {
//        return service.syncDocument();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                service.syncDocument();
            }
        });
        t.start();
        return new StatusDto(true);
    }


}