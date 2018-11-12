package com.ixinhoo.papers.dto.papers;

import java.util.List;

/**
 * 组卷--按照章节组卷--题型题量及章节范围 DTO
 *
 * @author cici
 */
public class QuestionTypeAndChapterDto implements java.io.Serializable{
    private Long questionSum;//试题总数
    private List<QuestionTypeDto> types;//题型
    private List<QuestionTypeDto> otherTypes;//其他题型
    private Long otherNum;//其他题型总数

    public Long getOtherNum() {
        return otherNum;
    }

    public void setOtherNum(Long otherNum) {
        this.otherNum = otherNum;
    }

    public Long getQuestionSum() {
        return questionSum;
    }

    public void setQuestionSum(Long questionSum) {
        this.questionSum = questionSum;
    }

    public List<QuestionTypeDto> getTypes() {
        return types;
    }

    public void setTypes(List<QuestionTypeDto> types) {
        this.types = types;
    }

    public List<QuestionTypeDto> getOtherTypes() {
        return otherTypes;
    }

    public void setOtherTypes(List<QuestionTypeDto> otherTypes) {
        this.otherTypes = otherTypes;
    }
}