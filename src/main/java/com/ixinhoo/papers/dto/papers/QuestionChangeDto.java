package com.ixinhoo.papers.dto.papers;

import com.ixinhoo.papers.dto.common.CommonIdAndNameDto;

import java.util.List;

/**
 * 题目换题--详情DTO
 *
 * @author cici
 */
public class QuestionChangeDto implements java.io.Serializable{
    private Long id;
    private Integer baseType;//基础题型
    private String baseTypeName;//基础题型名称
    private Long typeId;//题目题型id，为QuestionType中的id
    private String typeName;//题目题型名称
    private Integer difficult;//题目难度值
    private String difficultName;//难度名称
    private Integer examType;//考察类型
    private String examTypeName;//考察类型名称
    private String source;//试卷  来源
    private String content;//题目内容
    private List<CommonIdAndNameDto> knowledge;//知识点
    private String options;//选项的json字符串
    private String answer;//答案
    private String explanation;//解析

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<CommonIdAndNameDto> getKnowledge() {
        return knowledge;
    }

    public void setKnowledge(List<CommonIdAndNameDto> knowledge) {
        this.knowledge = knowledge;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }
}