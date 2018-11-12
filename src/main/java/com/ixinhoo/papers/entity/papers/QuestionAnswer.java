package com.ixinhoo.papers.entity.papers;

import com.chunecai.crumbs.api.entity.AuditEntity;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * 试题答案
 *
 * @author cici
 */
@Table(name = "question_answer")
public class QuestionAnswer extends AuditEntity {
    @Column
    private Long questionId;//试题id
    @Column
    private String content;//题目
    @Column
    private String options;//选项
    @Column
    private String answer;//答案
    @Column
    private String explanation;//解析
    @Column
    private String questionCode;//试题标识

    public String getQuestionCode() {
        return questionCode;
    }

    public void setQuestionCode(String questionCode) {
        this.questionCode = questionCode;
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