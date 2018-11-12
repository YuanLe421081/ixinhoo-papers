package com.ixinhoo.papers.entity.authority;

import tk.mybatis.mapper.annotation.NameStyle;
import tk.mybatis.mapper.code.Style;

import javax.persistence.Table;

/**
 * 角色菜单
 *
 * @author cici
 */
@Table(name = "role_menu")
@NameStyle(Style.camelhumpAndLowercase)
public class RoleMenu {
    private Long roleId;
    private Long menuId;

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }
}