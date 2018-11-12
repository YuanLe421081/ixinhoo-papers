package com.ixinhoo.shiro;

import com.chunecai.crumbs.api.shiro.ShiroUser;

/**
 * 自定义Authentication对象，
 * 使得Subject除了携带用户的登录名外还可以携带更多信息.
 *
 * @author cici
 */
public class PapersShiroUser extends ShiroUser {
    private Long roleId;//角色主键id


    public PapersShiroUser(Long id, Long roleId, String loginName, String name, String system) {
        super(id, loginName, name, system);
        this.roleId = roleId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
}