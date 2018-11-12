package com.ixinhoo.papers.controller.authority;


import com.chunecai.crumbs.api.client.DetailDto;
import com.chunecai.crumbs.api.code.DataTable;
import com.chunecai.crumbs.api.util.DataTableUtil;
import com.ixinhoo.papers.entity.authority.SystemUserOperate;
import com.ixinhoo.papers.service.authority.SystemUserOperateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/api/bg/system-user-operate")
//@RequiresRoles(value = "admin")
public class SystemUserOperateController {

    @Autowired
    private SystemUserOperateService service;


    @RequestMapping(value = "show/{id}", method = RequestMethod.GET)
    public DetailDto<SystemUserOperate> findById(@PathVariable("id") Long id) {
        return new DetailDto<>(true, service.findById(id));
    }


    @RequestMapping(value = "list", method = RequestMethod.POST)
    public DataTable<SystemUserOperate> list(HttpServletRequest request) {
        return service.listDatatable(DataTableUtil.toDataTable(request));
    }


}