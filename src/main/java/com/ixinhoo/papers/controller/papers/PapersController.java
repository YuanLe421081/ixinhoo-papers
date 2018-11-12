package com.ixinhoo.papers.controller.papers;


import com.chunecai.crumbs.api.client.DetailDto;
import com.chunecai.crumbs.api.client.StatusDto;
import com.chunecai.crumbs.api.code.DataTable;
import com.chunecai.crumbs.api.util.DataTableUtil;
import com.ixinhoo.papers.entity.papers.Papers;
import com.ixinhoo.papers.service.papers.PapersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/api/bg/papers")
public class PapersController {

    @Autowired
    private PapersService service;


    @RequestMapping(value = "show/{id}", method = RequestMethod.GET)
    public DetailDto<Papers> findById(@PathVariable("id") Long id) {
        return new DetailDto<>(true, service.findById(id));
    }


    @RequestMapping(value = "list", method = RequestMethod.POST)
    public DataTable<Papers> list(HttpServletRequest request) {
        return service.listDatatable(DataTableUtil.toDataTable(request));
    }

    @RequestMapping(value = "sync", method = RequestMethod.GET)
    public StatusDto sync() {
//        return service.syncPapers();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                service.syncPapers();
            }
        });
        t.start();
        return new StatusDto(true);
    }


}