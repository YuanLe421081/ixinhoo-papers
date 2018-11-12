package com.ixinhoo.papers.dto.papers;

/**
 * 试题搜索-请求DTO
 *
 * @author cici
 */
public class QuestionSearchReqDto implements java.io.Serializable{
    private Integer p;//当前页
    private Integer s;//页大小
    private Long userId;//用户主键--用于判断用户是否收藏此类题目
    private Integer stage;//学段
    private Long subjectId;//学科id
    private Integer grade;//年级
    private Integer term;//学期--预留
    private Long versionId;//版本id
    private Long chapterId;//章节id
    private Long knowledgeId;//知识点id
    private Integer examType;//题类（考察类型）
    private Long typeId;//题型id
    private Integer difficult;//题目难度
    private Integer knowledgeNum;//知识点数;0--全部，1--1个，2--2个，3--多个
    private String name;//搜索名称
    private String sortName;//排序名称，updatedAt--更新时间，usedNum--组卷次数
    private String desc;//排序,降序desc/升序asc

    public Long getKnowledgeId() {
        return knowledgeId;
    }

    public void setKnowledgeId(Long knowledgeId) {
        this.knowledgeId = knowledgeId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public Long getChapterId() {
        return chapterId;
    }

    public void setChapterId(Long chapterId) {
        this.chapterId = chapterId;
    }

    public Integer getExamType() {
        return examType;
    }

    public void setExamType(Integer examType) {
        this.examType = examType;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public Integer getDifficult() {
        return difficult;
    }

    public void setDifficult(Integer difficult) {
        this.difficult = difficult;
    }

    public Integer getKnowledgeNum() {
        return knowledgeNum;
    }

    public void setKnowledgeNum(Integer knowledgeNum) {
        this.knowledgeNum = knowledgeNum;
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
}