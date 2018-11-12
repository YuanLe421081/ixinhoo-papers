package com.ixinhoo.papers.entity.base;

import com.chunecai.crumbs.api.entity.AuditEntity;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * 地区
 *
 * @author cici
 */
@Table(name = "area")
public class Area extends AuditEntity {
    @Column
    private String name;//名称
    @Column
    private String code;//标识
    @Column
    private String sortName;//简称
    @Column
    private String pinyin;//拼音
    @Column
    private Long supId;//父级id
    @Column
    private Integer level;//级别， (1:省、2:市、3:区、4:街道/镇/乡)

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

    public String getSortName() {
        return sortName;
    }

    public void setSortName(String sortName) {
        this.sortName = sortName;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public Long getSupId() {
        return supId;
    }

    public void setSupId(Long supId) {
        this.supId = supId;
    }

    public Integer getLevel() { return level; }

    public void setLevel(Integer level) { this.level = level; }
}