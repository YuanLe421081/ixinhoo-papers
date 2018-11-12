package com.ixinhoo.papers.dto.user.paper;

import java.util.List;

/**
 * 用户模板保存DTO--题型
 *
 * @author 448778074@qq.com (cici)
 */
public class UserPaperTemplateSaveTypeDto implements java.io.Serializable{
    private Long id;//题型id
    private String name;//题型名称
    private List<UserPaperTemplateSaveTypeKnowledgeDto> questions;//题目

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<UserPaperTemplateSaveTypeKnowledgeDto> getQuestions() {
        return questions;
    }

    public void setQuestions(List<UserPaperTemplateSaveTypeKnowledgeDto> questions) {
        this.questions = questions;
    }
}
