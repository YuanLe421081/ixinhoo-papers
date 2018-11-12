package com.ixinhoo.papers.dto.user.paper;

import java.util.List;

/**
 * 用户模板保存DTO
 *
 * @author 448778074@qq.com (cici)
 */
public class UserPaperTemplateSaveDto implements java.io.Serializable{
    private Long userId;//用户id
    private Long id;//模板id
    private String title;//标题
    private Integer stage;//学段
    private Long subjectId;//学科id
    private String subjectName;//学科名称
    private Integer grade;//年级
    private Integer term;//学期
    private Long versionId;//版本id
    private List<UserPaperTemplateSaveTypeDto> types;//题型

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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<UserPaperTemplateSaveTypeDto> getTypes() {
        return types;
    }

    public void setTypes(List<UserPaperTemplateSaveTypeDto> types) {
        this.types = types;
    }
}
