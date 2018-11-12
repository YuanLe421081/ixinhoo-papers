package com.ixinhoo.papers.entity.papers;

import com.chunecai.crumbs.api.entity.AuditEntity;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * 试卷
 *
 * @author cici
 */
@Table(name = "papers")
public class Papers extends AuditEntity {
    @Column
    private Long typeId;//试卷类型id
    @Column
    private String typeName;//试卷类型名称
    @Column
    private Integer stage;//学段
    @Column
    private Long subjectId;//学科id
    @Column
    private Long bookId;//册别id
    @Column
    private String bookPath;//册别路径
    @Column
    private Long versionId;//版本id
    @Column
    private String versionPath;//版本路径
    @Column
    private String title;//标题
    @Column
    private Integer year;//年份
    @Column
    private Integer shareStatus;//是否分享;0用户私有，1通过审核，2,未通过审核,3等待审核，4回收站
    @Column
    private Integer auditStatus;//审核状态,3待审核，1审核通过，2审核不通过
    @Column
    private String code;//唯一标识
    @Column
    private Long createdAt;//创建时间
    @Column
    private Long updatedAt;//更新时间
    private Integer grade;//年级

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Integer getStage() {
        return stage;
    }

    public void setStage(Integer stage) {
        this.stage = stage;
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public String getBookPath() {
        return bookPath;
    }

    public void setBookPath(String bookPath) {
        this.bookPath = bookPath;
    }

    public Long getVersionId() {
        return versionId;
    }

    public void setVersionId(Long versionId) {
        this.versionId = versionId;
    }

    public String getVersionPath() {
        return versionPath;
    }

    public void setVersionPath(String versionPath) {
        this.versionPath = versionPath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getShareStatus() {
        return shareStatus;
    }

    public void setShareStatus(Integer shareStatus) {
        this.shareStatus = shareStatus;
    }

    public Integer getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(Integer auditStatus) {
        this.auditStatus = auditStatus;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }
}