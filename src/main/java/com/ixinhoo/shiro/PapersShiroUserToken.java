package com.ixinhoo.shiro;

import com.chunecai.crumbs.api.shiro.ShiroUserToken;

/**
 * @author 448778074@qq.com (cici)
 */
public class PapersShiroUserToken extends ShiroUserToken {
    private Long roleId;

    public PapersShiroUserToken(Long id, String username, String password, String name, String system, Long roleId) {
        super(id, username, password, name, system);
        this.roleId = roleId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
}
