package com.ixinhoo.papers.controller.base;


import com.chunecai.crumbs.api.client.DetailDto;
import com.chunecai.crumbs.api.client.StatusDto;
import com.chunecai.crumbs.api.code.DataTable;
import com.chunecai.crumbs.api.util.DataTableUtil;
import com.ixinhoo.papers.entity.base.School;
import com.ixinhoo.papers.service.base.SchoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(value = "/api/bg/school")
public class SchoolController {

    @Autowired
    private SchoolService service;


    @RequestMapping(value = "save", method = RequestMethod.POST)
    public StatusDto save(School entity) {
        service.save(entity);
        return new StatusDto(true);
    }


    @RequestMapping(value = "show/{id}", method = RequestMethod.GET)
    public DetailDto<School> findById(@PathVariable("id") Long id) {
        return new DetailDto<>(true, service.findById(id));
    }

    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public StatusDto delete(@RequestParam("ids") List<Long> ids) {
        service.deleteByIds(ids);
        return new StatusDto(true);
    }


    @RequestMapping(value = "list", method = RequestMethod.POST)
    public DataTable<School> list(HttpServletRequest request) {
        return service.listDatatable(DataTableUtil.toDataTable(request));
    }

}