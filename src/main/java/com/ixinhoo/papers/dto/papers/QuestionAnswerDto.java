package com.ixinhoo.papers.dto.papers;

/**
 * 试题答案DTO
 *
 * @author cici
 */
public class QuestionAnswerDto implements java.io.Serializable{
    private Long id;//主键
    private Long questionId;//试题id
    private String content;//题目
    private String options;//选项
    private String answer;//答案
    private String explanation;//解析
    private String examPoint;//考点

    public String getExamPoint() {
        return examPoint;
    }

    public void setExamPoint(String examPoint) {
        this.examPoint = examPoint;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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