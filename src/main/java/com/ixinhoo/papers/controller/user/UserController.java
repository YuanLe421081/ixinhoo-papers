package com.ixinhoo.papers.controller.user;


import com.chunecai.crumbs.api.client.DetailDto;
import com.chunecai.crumbs.api.client.StatusDto;
import com.chunecai.crumbs.api.code.DataTable;
import com.chunecai.crumbs.api.util.DataTableUtil;
import com.ixinhoo.papers.dto.user.UserDto;
import com.ixinhoo.papers.entity.user.User;
import com.ixinhoo.papers.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(value = "/api/bg/user")
public class UserController {

    @Autowired
    private UserService service;


    @RequestMapping(value = "show/{id}", method = RequestMethod.GET)
    public DetailDto<UserDto> findById(@PathVariable("id") Long id) {
        return service.findUserInfoById(id);
    }


    @RequestMapping(value = "list", method = RequestMethod.POST)
    public DataTable<User> list(HttpServletRequest request) {
        return service.listDatatable(DataTableUtil.toDataTable(request));
    }

    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public StatusDto delete(@RequestParam("ids") List<Long> ids) {
         service.deleteByIds(ids);
         return new StatusDto(true);
    }


    @RequestMapping(value = "status", method = RequestMethod.POST)
    public StatusDto status(@RequestParam("ids") List<Long> ids,@RequestParam("status") Integer status) {
        return service.updateStatusByIds(ids,status);
    }



}