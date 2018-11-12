package com.ixinhoo.papers.controller;


import com.chunecai.crumbs.api.client.DetailDto;
import com.chunecai.crumbs.api.client.StatusDto;
import com.chunecai.crumbs.code.util.http.NetworkUtil;
import com.ixinhoo.papers.dto.authority.SystemUserDto;
import com.ixinhoo.papers.entity.authority.SystemUserOperate;
import com.ixinhoo.papers.service.authority.SystemUserService;
import org.apache.http.HttpRequest;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


@RestController
public class ShiroController {

    @Autowired
    private SystemUserService service;


    @RequestMapping(value = "/api/bg/login-validate", method = RequestMethod.GET)
    public StatusDto login() {
        Subject s = SecurityUtils.getSubject();
        return s.isAuthenticated() ? new StatusDto(true) :  new StatusDto(false);
    }

    @RequestMapping(value = "/api/bg/login", method = RequestMethod.POST)
    public DetailDto<SystemUserDto> login(SystemUserDto userDto, HttpServletRequest request) {
        return service.loginUser(userDto,NetworkUtil.ipAddress(request));
    }

    @RequestMapping(value = "/api/bg/logout", method = RequestMethod.GET)
    public StatusDto logout(HttpServletRequest request) {
        service.logout(NetworkUtil.ipAddress(request));
        return new StatusDto(true);
    }


}