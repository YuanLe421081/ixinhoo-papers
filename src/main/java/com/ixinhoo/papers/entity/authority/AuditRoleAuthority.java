package com.ixinhoo.papers.entity.authority;

import com.chunecai.crumbs.api.entity.AuditEntity;
import tk.mybatis.mapper.annotation.NameStyle;
import tk.mybatis.mapper.code.Style;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * 审批用户资料上传权限记录
 *
 * @author 448778074@qq.com (cici)
 */
@Table(name = "audit_role_authority")
@NameStyle(Style.camelhumpAndLowercase)
public class AuditRoleAuthority extends AuditEntity {
    @Column
    private Long userId;//用户id
    @Column
    private Long roleId;//角色id
    @Column
    private Long time;//审批时间
    @Column
    private String remark;//审批描述
    @Column
    private Integer status;//审批状态,1通过、2不通过

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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
