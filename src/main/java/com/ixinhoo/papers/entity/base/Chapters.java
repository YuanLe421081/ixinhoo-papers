package com.ixinhoo.papers.entity.base;

import com.chunecai.crumbs.api.entity.AuditEntity;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * 章节、册别、版本
 *
 * @author cici
 */
@Table(name = "chapters")
public class Chapters extends AuditEntity {
    @Column
    private String name;//名称
    @Column
    private Long supId;//父级id
    @Column
    private String path;//路径
    @Column
    private Long subjectId;//学科id
    @Column
    private String subjectName;//学科名称
    @Column
    private Long sort;//排序
    @Column
    private Integer stage;//学段
    @Column
    private Integer grade;//年级
    @Column
    private Integer type;//类型（1版本、2册别、3章节、4备考）
    @Column
    private String code;//标识
    @Column
    private Integer status;//状态，1可用、2禁用
    @Column
    private Integer term;//学期；0--全册、1-上学期-上册、2-下学期-下册

    public Integer getTerm() {
        return term;
    }

    public void setTerm(Integer term) {
        this.term = term;
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

    public Long getSupId() {
        return supId;
    }

    public void setSupId(Long supId) {
        this.supId = supId;
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

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public Long getSort() {
        return sort;
    }

    public void setSort(Long sort) {
        this.sort = sort;
    }

    public Integer getStage() {
        return stage;
    }

    public void setStage(Integer stage) {
        this.stage = stage;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}