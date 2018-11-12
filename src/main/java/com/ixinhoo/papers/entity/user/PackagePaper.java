package com.ixinhoo.papers.entity.user;

import com.chunecai.crumbs.api.entity.AuditEntity;

import javax.persistence.Table;

/**
 * 用户组卷;
 * template模板,1密封线，2考试时间，3大题评分区，4分卷注释，5主标题，6考生填写，7注意事项，
 * 8分大题，9副标题，10总评分，11分卷，12大题注释
 * paperSize:1-A4纸张，2-A5纸张
 * paperType:试卷类型，1答案，2考点，3解析
 * paperFile:试卷下载格式，1-word，2-ppt
 *
 * @author cici
 */
@Table(name = "package_paper")
public class PackagePaper extends AuditEntity {
    private Long userId;//用户id
    private String paperTitle;//试卷标题
    private String question;//类型&问题分数json
    private String template;//模板数组json
    private Integer paperSize;//1-A4纸张，2-A3双栏纸张,3-B5纸张，4-B4双栏；任选其一
    private String paperType;//试卷类型，1答案，2考点，3解析；json数组
    private Integer paperFile;//试卷下载格式，1-word，2-ppt
    private Integer examStructure;//考试结构,1-考试模板,2-测试模板,3-作业模板
    private Integer typeStyle;//用卷类型,1-教师用卷、2-学生用卷
    private Integer stage;//学段
    private Long subjectId;//学科
    private String subjectName;//学科名称
    private Integer term;//学期
    private Long time;//组卷时间
    private String remark;//组卷信息-备注注释信息json字符串

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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
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

    public Integer getTerm() {
        return term;
    }

    public void setTerm(Integer term) {
        this.term = term;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
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

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public Integer getPaperSize() {
        return paperSize;
    }

    public void setPaperSize(Integer paperSize) {
        this.paperSize = paperSize;
    }

    public String getPaperType() {
        return paperType;
    }

    public void setPaperType(String paperType) {
        this.paperType = paperType;
    }

    public Integer getPaperFile() {
        return paperFile;
    }

    public void setPaperFile(Integer paperFile) {
        this.paperFile = paperFile;
    }
}