package com.ixinhoo.papers.entity.user;

import com.chunecai.crumbs.api.entity.AuditEntity;

import javax.persistence.Table;

/**
 * 用户试卷模板--题型-小题
 *
 * @author 448778074@qq.com (cici)
 */
@Table(name = "user_paper_template_question")
public class UserPaperTemplateQuestion extends AuditEntity {
    private Long templateId;//模板id
    private Long typeId;//大题主键id
    private Integer sort;//排序
    private String knowledgeIds;//知识点id集合
    private String knowledgeNames;//知识点名称集合
    private Integer difficult;//难度

    public Integer getDifficult() {
        return difficult;
    }

    public void setDifficult(Integer difficult) {
        this.difficult = difficult;
    }

    public String getKnowledgeNames() {
        return knowledgeNames;
    }

    public void setKnowledgeNames(String knowledgeNames) {
        this.knowledgeNames = knowledgeNames;
    }

    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getKnowledgeIds() {
        return knowledgeIds;
    }

    public void setKnowledgeIds(String knowledgeIds) {
        this.knowledgeIds = knowledgeIds;
    }
}