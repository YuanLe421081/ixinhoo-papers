package com.ixinhoo.papers.entity.authority;

import com.chunecai.crumbs.api.entity.AuditEntity;

import javax.persistence.Table;

/**
 * 文档资料审核记录
 *
 * @author 448778074@qq.com (cici)
 */
@Table(name = "document_audit_record")
public class DocumentAuditRecord extends AuditEntity {
    private Long userId;//申请人id
    private String userName;//申请人名称
    private Long auditId;//审核人id
    private String auditName;//审核人名称
    private Integer auditUserType;//审核人类型
    private Long time;//审核时间
    private String remark;//审核描述
    private Integer status;//审批状态,1通过、2不通过
    private Long documentId;//资料id
    private String documentAddress;//资料下载地址
    private String documentTitle;//资料标题

    public String getDocumentTitle() {
        return documentTitle;
    }

    public void setDocumentTitle(String documentTitle) {
        this.documentTitle = documentTitle;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getAuditId() {
        return auditId;
    }

    public void setAuditId(Long auditId) {
        this.auditId = auditId;
    }

    public String getAuditName() {
        return auditName;
    }

    public void setAuditName(String auditName) {
        this.auditName = auditName;
    }

    public Integer getAuditUserType() {
        return auditUserType;
    }

    public void setAuditUserType(Integer auditUserType) {
        this.auditUserType = auditUserType;
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

    public Long getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }

    public String getDocumentAddress() {
        return documentAddress;
    }

    public void setDocumentAddress(String documentAddress) {
        this.documentAddress = documentAddress;
    }
}
