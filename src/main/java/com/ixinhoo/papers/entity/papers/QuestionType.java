package com.ixinhoo.papers.entity.papers;

import com.chunecai.crumbs.api.entity.AuditEntity;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * 题目题型
 *
 * @author cici
 */
@Table(name = "question_type")
public class QuestionType extends AuditEntity {
    @Column
    private String name;//名称
    @Column
    private String code;//标识


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}