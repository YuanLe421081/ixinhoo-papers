package com.ixinhoo.papers.dto.authority;


import java.util.List;

/**
 * 系统用户dto
 *
 * @author 448778074@qq.com (cici)
 */
public class SystemUserDto implements java.io.Serializable{
    private Long id;
    private String loginName;//登录账号
    private String password;//登录密码
    private String name;//用户名
    private Integer status;//状态  （启用  与未启用）
    private Long roleId;//角色主键id
    private List<Long> menuIds;//菜单id集合

    public List<Long> getMenuIds() {
        return menuIds;
    }

    public void setMenuIds(List<Long> menuIds) {
        this.menuIds = menuIds;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
