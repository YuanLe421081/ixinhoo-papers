package com.ixinhoo.papers.dto.papers;

/**
 * 试卷模板请求DTO;
 * // data:{"userId":1,"stage":1,"subjectId":2,"grade":1,"term":1,"versionId":1},
 *
 * @author 448778074@qq.com (cici)
 */
public class PaperTemplateReqDto implements java.io.Serializable{
    private Long userId;//用户主键
    private Integer stage;//学段
    private Long subjectId;//学科id
    private Integer grade;//年级
    private Integer term;//学期
    private Long versionId;//版本id

    private Integer p;//当前页--模板列表使用
    private Integer s;//页大小--模板列表使用
    private Long typeId;//模板列表使用

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public Long getVersionId() {
        return versionId;
    }

    public void setVersionId(Long versionId) {
        this.versionId = versionId;
    }

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
}
