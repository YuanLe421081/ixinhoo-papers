package com.ixinhoo.papers.controller.authority;


import com.chunecai.crumbs.api.client.DetailDto;
import com.chunecai.crumbs.api.client.StatusDto;
import com.chunecai.crumbs.api.code.DataTable;
import com.chunecai.crumbs.api.util.DataTableUtil;
import com.chunecai.crumbs.code.util.mapper.BeanMapper;
import com.ixinhoo.papers.dto.authority.SystemUserDto;
import com.ixinhoo.papers.service.authority.SystemUserService;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(value = "/api/bg/system-user")
//@RequiresRoles(value = "admin")
public class SystemUserController {

    @Autowired
    private SystemUserService service;


    @RequestMapping(value = "save", method = RequestMethod.POST)
    public StatusDto save(SystemUserDto userDto) {
        return service.saveSystemUser(userDto);
    }


    @RequestMapping(value = "show/{id}", method = RequestMethod.GET)
    public DetailDto<SystemUserDto> findById(@PathVariable("id") Long id) {
        return service.findSystemUserDtoById(id);
    }

    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public StatusDto delete(@RequestParam("ids") List<Long> ids) {
        return service.deleteSystemUserByIds(ids);
    }


    @RequestMapping(value = "list", method = RequestMethod.POST)
    public DataTable<SystemUserDto> list(HttpServletRequest request) {
        return service.listSystemUser(DataTableUtil.toDataTable(request));
    }

    @RequestMapping(value = "test")
    public StatusDto test() {
        return service.test();
    }

}