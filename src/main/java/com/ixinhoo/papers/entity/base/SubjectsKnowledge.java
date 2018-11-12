package com.ixinhoo.papers.entity.base;

import com.chunecai.crumbs.api.entity.AuditEntity;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * 科目知识点
 *
 * @author cici
 */
@Table(name = "subjects_knowledge")
public class SubjectsKnowledge extends AuditEntity {
    @Column
    private String name;//名称
    @Column
    private String code;//标识
    @Column
    private Long supId;//父节点id
    @Column
    private Integer stage;//学段
    @Column
    private Long sort;//排序
    @Column
    private String path;//路径
    @Column
    private Long subjectId;//学科id
    @Column
    private String subjectName;//学科名称

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public Long getSupId() {
        return supId;
    }

    public void setSupId(Long supId) {
        this.supId = supId;
    }

    public Integer getStage() {
        return stage;
    }

    public void setStage(Integer stage) {
        this.stage = stage;
    }

    public Long getSort() {
        return sort;
    }

    public void setSort(Long sort) {
        this.sort = sort;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

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
}