package com.ixinhoo.papers.entity.base;

import com.chunecai.crumbs.api.entity.AuditEntity;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * 学段学科
 *
 * @author cici
 */
@Table(name = "stage_subjects")
public class StageSubjects extends AuditEntity {
    @Column
    private String name;//名称
    @Column
    private String code;//标识
    @Column
    private Integer stage;//所属学段
    @Column
    private Long subjectId;//学科id

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

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

    public Integer getStage() {
        return stage;
    }

    public void setStage(Integer stage) {
        this.stage = stage;
    }
}