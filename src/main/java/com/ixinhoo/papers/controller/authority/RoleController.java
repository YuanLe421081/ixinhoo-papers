package com.ixinhoo.papers.controller.authority;


import com.chunecai.crumbs.api.client.DetailDto;
import com.chunecai.crumbs.api.client.ListDto;
import com.chunecai.crumbs.api.client.StatusDto;
import com.chunecai.crumbs.api.code.DataTable;
import com.chunecai.crumbs.api.util.DataTableUtil;
import com.chunecai.crumbs.code.util.mapper.BeanMapper;
import com.ixinhoo.papers.dto.authority.RoleMenuDto;
import com.ixinhoo.papers.dto.authority.SystemUserDto;
import com.ixinhoo.papers.entity.authority.Role;
import com.ixinhoo.papers.entity.authority.UserRole;
import com.ixinhoo.papers.service.authority.RoleService;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(value = "/api/bg/system-role")
//@RequiresRoles(value = "admin")
public class RoleController {

    @Autowired
    private RoleService service;


    @RequestMapping(value = "save-menu", method = RequestMethod.POST)
    public StatusDto saveMenu(@RequestParam("roleId")Long roleId,@RequestParam("menuIds")List<Long> menuIds) {
        return service.saveRoleMenu(roleId,menuIds);
    }

    @RequestMapping(value = "save", method = RequestMethod.POST)
    public StatusDto save(Role role) {
         service.save(role);
         return new StatusDto(true);
    }

    @RequestMapping(value = "show/{id}", method = RequestMethod.GET)
    public DetailDto<RoleMenuDto> findById(@PathVariable("id") Long id) {
        return service.findRoleById(id);
    }



    @RequestMapping(value = "list-admin", method = RequestMethod.POST)
    public DataTable<Role> listAdmin() {
        return service.listDatatableRole();
    }

    @RequestMapping(value = "list-notice", method = RequestMethod.POST)
    public DataTable<Role> list(HttpServletRequest request) {
        return service.listDatatableRoleNotice(DataTableUtil.toDataTable(request));
    }

    @RequestMapping(value = "list-notice-all", method = RequestMethod.GET)
    public ListDto<Role> list() {
        return service.listAllRoleNotice();
    }

    @RequestMapping(value = "user-role", method = RequestMethod.POST)
    public ListDto<UserRole> findUserRole(Long userId) {
        return service.findUserRoleByUserId(userId);
    }


    @RequestMapping(value = "save-user", method = RequestMethod.POST)
    public StatusDto saveUserRole(@RequestParam("userId")Long userId,@RequestParam("roleIds")List<Long> roleIds) {
        return service.saveUserRole(userId,roleIds);
    }


}