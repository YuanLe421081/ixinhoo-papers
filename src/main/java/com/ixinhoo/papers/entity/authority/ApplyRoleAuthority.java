package com.ixinhoo.papers.entity.authority;

import com.chunecai.crumbs.api.entity.AuditEntity;
import tk.mybatis.mapper.annotation.NameStyle;
import tk.mybatis.mapper.code.Style;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * 用户申请资料上传权限记录
 *
 * @author 448778074@qq.com (cici)
 */
@Table(name = "apply_role_authority")
@NameStyle(Style.camelhumpAndLowercase)
public class ApplyRoleAuthority extends AuditEntity {
    @Column
    private Long userId;//用户id
    @Column
    private Long roleId;//角色id
    @Column
    private Long time;//申请时间
    @Column
    private String remark;//申请描述

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

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
