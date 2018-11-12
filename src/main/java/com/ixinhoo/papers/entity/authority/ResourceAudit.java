package com.ixinhoo.papers.entity.authority;

import com.chunecai.crumbs.api.entity.AuditEntity;
import tk.mybatis.mapper.annotation.NameStyle;
import tk.mybatis.mapper.code.Style;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * 资源审核记录
 *
 * @author 448778074@qq.com (cici)
 */
@Table(name = "resource_audit")
@NameStyle(Style.camelhumpAndLowercase)
public class ResourceAudit extends AuditEntity {
    @Column
    private Long applyUserId;//申请人用户id
    @Column
    private Long userId;//审核人用户id
    @Column
    private Integer userType;//用户类型；1网站用户、2管理员用户
    @Column
    private Long time;//审核时间
    @Column
    private String remark;//审核描述
    @Column
    private Integer status;//审批状态,1通过、2不通过
    @Column
    private Long dataId;//资料id
    @Column
    private String downloadUrl;//下载路径地址

    public Long getApplyUserId() {
        return applyUserId;
    }

    public void setApplyUserId(Long applyUserId) {
        this.applyUserId = applyUserId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
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

    public Long getDataId() {
        return dataId;
    }

    public void setDataId(Long dataId) {
        this.dataId = dataId;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }
}
