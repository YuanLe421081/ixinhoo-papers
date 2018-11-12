package com.ixinhoo.papers.dto.user.paper;

import java.util.List;

/**
 * 保存用户组卷信息
 *
 * @author 448778074@qq.com (cici)
 */
public class PackagePaperSaveDto implements java.io.Serializable{
    private Long id;//主键id
    private Long userId;//用户id
    private String paperTitle;//试卷标题
    private List<Integer> template;//模板数组json
    private Integer paperSize;//1-A4纸张，2-A3双栏纸张,3-B5纸张，4-B4双栏
    private List<Integer> paperType;//试卷类型，1答案，2考点，3解析
    private Integer paperFile;//试卷下载格式，1-word，2-ppt
    private Integer examStructure;//考试结构,1-考试模板,2-测试模板,3-作业模板
    private Integer typeStyle;//用卷类型,1-教师用卷、2-学生用卷
    private Integer stage;//学段
    private Long subjectId;//学科
    private String subjectName;//学科名称
    private Integer term;//学期
    private Long versionId;//版本
    private Integer grade;//年级
    private List<PackagePaperSaveTypeDto> type;//类型&问题分数json
    private PackagePaperSaveRemarkDto remark;//备注信息

    public Integer getExamStructure() {
        return examStructure;
    }

    public void setExamStructure(Integer examStructure) {
        this.examStructure = examStructure;
    }

    public Integer getTypeStyle() {
        return typeStyle;
    }

    public void setTypeStyle(Integer typeStyle) {
        this.typeStyle = typeStyle;
    }

    public PackagePaperSaveRemarkDto getRemark() {
        return remark;
    }

    public void setRemark(PackagePaperSaveRemarkDto remark) {
        this.remark = remark;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public Long getVersionId() {
        return versionId;
    }

    public void setVersionId(Long versionId) {
        this.versionId = versionId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getPaperTitle() {
        return paperTitle;
    }

    public void setPaperTitle(String paperTitle) {
        this.paperTitle = paperTitle;
    }

    public List<Integer> getTemplate() {
        return template;
    }

    public void setTemplate(List<Integer> template) {
        this.template = template;
    }

    public Integer getPaperSize() {
        return paperSize;
    }

    public void setPaperSize(Integer paperSize) {
        this.paperSize = paperSize;
    }

    public List<Integer> getPaperType() {
        return paperType;
    }

    public void setPaperType(List<Integer> paperType) {
        this.paperType = paperType;
    }

    public Integer getPaperFile() {
        return paperFile;
    }

    public void setPaperFile(Integer paperFile) {
        this.paperFile = paperFile;
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

    public Integer getTerm() {
        return term;
    }

    public void setTerm(Integer term) {
        this.term = term;
    }


    public List<PackagePaperSaveTypeDto> getType() {
        return type;
    }

    public void setType(List<PackagePaperSaveTypeDto> type) {
        this.type = type;
    }
}
