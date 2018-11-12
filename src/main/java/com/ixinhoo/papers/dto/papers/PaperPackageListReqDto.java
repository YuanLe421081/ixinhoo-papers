package com.ixinhoo.papers.dto.papers;

import java.util.List;

/**
 * 组卷-试卷列表-请求DTO
 *
 * @author cici
 */
public class PaperPackageListReqDto implements java.io.Serializable{
    private Integer p;//当前页
    private Integer s;//页大小
    private Integer stage;//学段
    private Integer grade;//年级
    private Integer term;//学期
    private Long subjectId;//学科id
    private Long versionId;//版本id
    private List<Integer> typeIds;//类型id集合
    private Long chapterId;//章节id
    private String name;//搜索名称
    private String sortName;//排序名称，updatedAt--更新时间，downloadNum--下载量，score评分，price价格
    private String desc;//排序,降序desc/升序asc
    private Long provinceId;//省份id

    public Integer getP() {
        return p;
    }

    public void setP(Integer p) {
        this.p = p;
    }

    public Integer getS() {
        return s;
    }

    public void setS(Integer s) {
        this.s = s;
    }

    public Integer getStage() {
        return stage;
    }

    public void setStage(Integer stage) {
        this.stage = stage;
    }

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

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    public Long getVersionId() {
        return versionId;
    }

    public void setVersionId(Long versionId) {
        this.versionId = versionId;
    }

    public List<Integer> getTypeIds() {
        return typeIds;
    }

    public void setTypeIds(List<Integer> typeIds) {
        this.typeIds = typeIds;
    }

    public Long getChapterId() {
        return chapterId;
    }

    public void setChapterId(Long chapterId) {
        this.chapterId = chapterId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSortName() {
        return sortName;
    }

    public void setSortName(String sortName) {
        this.sortName = sortName;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Long getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Long provinceId) {
        this.provinceId = provinceId;
    }
}