package com.ixinhoo.papers.entity.papers;

import com.chunecai.crumbs.api.entity.AuditEntity;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * 题目
 *
 * @author cici
 */
@Table(name = "question")
public class Question extends AuditEntity {
    @Column
    private Long paperId;//试卷主键id
    @Column
    private Integer baseType;//基础题型
    @Column
    private String baseTypeName;//基础题型名称
    @Column
    private Long typeId;//题目题型id，为QuestionType中的id
    @Column
    private String typeName;//题目题型名称
    @Column
    private Integer stage;//学段
    @Column
    private Long subjectId;//学科id
    @Column
    private Integer auditStatus;//审核状态,3待审核，1审核通过，2审核不通过
    @Column
    private Long supId;//父级id
    @Column
    private Integer difficult;//题目难度值
    @Column
    private String difficultName;//难度名称
    @Column
    private Integer examType;//考察类型
    @Column
    private String examTypeName;//考察类型名称
    @Column
    private Integer grade;//适用年级
    @Column
    private String source;//试卷  来源
    @Column
    private Integer status;//试题状态
    @Column
    private String content;//题目内容
    @Column
    private Integer knowledgeNum;//知识点个数
    @Column
    private String code;//唯一标识
    @Column
    private Long createdAt;//创建时间
    @Column
    private String options;//选项的json字符串

    public Long getPaperId() {
        return paperId;
    }

    public void setPaperId(Long paperId) {
        this.paperId = paperId;
    }

    public Integer getBaseType() {
        return baseType;
    }

    public void setBaseType(Integer baseType) {
        this.baseType = baseType;
    }

    public String getBaseTypeName() {
        return baseTypeName;
    }

    public void setBaseTypeName(String baseTypeName) {
        this.baseTypeName = baseTypeName;
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

    public Integer getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(Integer auditStatus) {
        this.auditStatus = auditStatus;
    }

    public Long getSupId() {
        return supId;
    }

    public void setSupId(Long supId) {
        this.supId = supId;
    }

    public Integer getDifficult() {
        return difficult;
    }

    public void setDifficult(Integer difficult) {
        this.difficult = difficult;
    }

    public String getDifficultName() {
        return difficultName;
    }

    public void setDifficultName(String difficultName) {
        this.difficultName = difficultName;
    }

    public Integer getExamType() {
        return examType;
    }

    public void setExamType(Integer examType) {
        this.examType = examType;
    }

    public String getExamTypeName() {
        return examTypeName;
    }

    public void setExamTypeName(String examTypeName) {
        this.examTypeName = examTypeName;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getKnowledgeNum() {
        return knowledgeNum;
    }

    public void setKnowledgeNum(Integer knowledgeNum) {
        this.knowledgeNum = knowledgeNum;
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


    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }
}