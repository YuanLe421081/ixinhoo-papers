package com.ixinhoo.papers.entity.authority;

import com.chunecai.crumbs.api.entity.AuditEntity;
import tk.mybatis.mapper.annotation.NameStyle;
import tk.mybatis.mapper.code.Style;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * 用户角色
 * @author 448778074@qq.com (cici)
 */
@Table(name = "user_role")
@NameStyle(Style.camelhumpAndLowercase)
public class UserRole extends AuditEntity {
    @Column
    private Long userId;//用户id
    @Column
    private Long roleId;//角色id
    @Column
    private Integer type;//类型,1网站用户、2管理员用户

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
