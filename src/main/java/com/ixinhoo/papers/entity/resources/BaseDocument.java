package com.ixinhoo.papers.entity.resources;

import com.chunecai.crumbs.api.entity.AuditEntity;

import javax.persistence.Column;

/**
 * 资源文档--基类
 *
 * @author cici
 */
public class BaseDocument extends AuditEntity {
    @Column
    private Integer stage;//学段
    @Column
    private Long subjectId;//学科id
    @Column
    private String subjectName;//学科名称
    @Column
    private Long versionId;//版本id
    @Column
    private String versionName;//版本名称
    @Column
    private Long userId;//上传用户的主键id
    @Column
    private String userName;//上传用户姓名
    @Column
    private String title;//标题
    @Column
    private Long createdAt;//创建时间
    @Column
    private Long updatedAt;//更新时间
    @Column
    private Integer coin;//资料所需备课币
    @Column
    private Integer year;//年份
    @Column
    private Integer typeId;//类型
    @Column
    private String typeName;//类型名称
    @Column
    private String intro;//资料简介
    @Column
    private Integer source;//资源来源，1-别处引用、2-自己原创、3-改编整理、4-授权行使、5-其他、6-21世纪导入
    @Column
    private Long provinceId;//省份id
    @Column
    private String provinceName;//省份名称
    @Column
    private String fileType;//文件类型(ppt, doc, rar, video)
    @Column
    private Integer viewFlag;//是否有预览，1--有、2-没有
    @Column
    private String code;//标识
    @Column
    private Integer status;//状态
    @Column
    private String coverImage;//封面--预留
    @Column
    private String keyword;//关键字
    @Column
    private Integer term;//学期
    private Integer grade;//年级

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }
    public Integer getTerm() {
        return term;
    }

    public void setTerm(Integer term) {
        this.term = term;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public Long getVersionId() {
        return versionId;
    }

    public void setVersionId(Long versionId) {
        this.versionId = versionId;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
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

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public Integer getCoin() {
        return coin;
    }

    public void setCoin(Integer coin) {
        this.coin = coin;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public Long getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Long provinceId) {
        this.provinceId = provinceId;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public Integer getViewFlag() {
        return viewFlag;
    }

    public void setViewFlag(Integer viewFlag) {
        this.viewFlag = viewFlag;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}