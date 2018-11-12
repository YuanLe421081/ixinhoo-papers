package com.ixinhoo.papers.controller.authority;


import com.chunecai.crumbs.api.code.DataTable;
import com.chunecai.crumbs.api.util.DataTableUtil;
import com.ixinhoo.papers.entity.authority.DocumentAuditRecord;
import com.ixinhoo.papers.service.authority.DocumentAuditRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/api/bg/document-audit")
public class DocumentAuditRecordController {

    @Autowired
    private DocumentAuditRecordService service;


    @RequestMapping(value = "list", method = RequestMethod.POST)
    public DataTable<DocumentAuditRecord> list(HttpServletRequest request) {
        return service.listDatatable(DataTableUtil.toDataTable(request));
    }

}