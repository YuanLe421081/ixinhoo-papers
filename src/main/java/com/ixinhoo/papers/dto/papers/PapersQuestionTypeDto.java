package com.ixinhoo.papers.dto.papers;

import java.util.List;

/**
 * 试卷-试题类型
 *
 * @author 448778074@qq.com (cici)
 */
public class PapersQuestionTypeDto implements java.io.Serializable{
    private Long typeId;//类型id
    private String typeName;//类型名称
    private List<PapersQuestionContentDto> questions;//试题列表

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

    public List<PapersQuestionContentDto> getQuestions() {
        return questions;
    }

    public void setQuestions(List<PapersQuestionContentDto> questions) {
        this.questions = questions;
    }
}
