package com.ixinhoo.papers.dto.papers;

/**
 * 试题搜索-响应DTO
 *
 * @author cici
 */
public class QuestionSearchRspDto implements java.io.Serializable{
    private Long id;//主键
    private Long paperId;//试卷主键id
    private Integer baseType;//基础题型
    private String baseTypeName;//基础题型名称
    private Long typeId;//题目题型id，为QuestionType中的id
    private String typeName;//题目题型名称
    private Integer stage;//学段
    private Long subjectId;//学科id
    private Integer difficult;//题目难度值
    private String difficultName;//难度名称
    private Integer examType;//考察类型
    private String examTypeName;//考察类型名称
    private Integer grade;//适用年级
    private String source;//试卷  来源
    private String content;//题目内容
    private Integer knowledgeNum;//知识点个数
    private Long usedNum;//组卷次数
    private Boolean isCollection=false;//是否收藏
    private String options;//选项
    //TODO cici 加入选项


    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public Boolean getCollection() {
        return isCollection;
    }

    public void setCollection(Boolean collection) {
        isCollection = collection;
    }

    public Long getUsedNum() {
        return usedNum;
    }

    public void setUsedNum(Long usedNum) {
        this.usedNum = usedNum;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
}