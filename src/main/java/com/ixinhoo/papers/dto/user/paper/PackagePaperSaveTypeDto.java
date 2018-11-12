package com.ixinhoo.papers.dto.user.paper;

import java.util.List;

/**
 * 保存用户组卷信息-类型
 *
 * @author 448778074@qq.com (cici)
 */
public class PackagePaperSaveTypeDto implements java.io.Serializable{
    private Long id;//类型id
    private String name;//名称
    private List<PackagePaperSaveQuestionDto> question;//问题集合

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PackagePaperSaveQuestionDto> getQuestion() {
        return question;
    }

    public void setQuestion(List<PackagePaperSaveQuestionDto> question) {
        this.question = question;
    }
}
