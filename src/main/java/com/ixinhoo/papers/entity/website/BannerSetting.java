package com.ixinhoo.papers.entity.website;

import com.chunecai.crumbs.api.entity.AuditEntity;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * 广告banner设置、广告管理
 *
 * @author cici
 */
@Table(name = "banner_setting")
public class BannerSetting extends AuditEntity {
    @Column
    private Integer sort;//排序
    @Column
    private Integer type;//banner类型。1首页轮播、2组卷轮播
    @Column
    private Long dataId;//数据id
    @Column
    private Integer status;//状态
    @Column
    private String link;//链接地址

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }


    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getDataId() {
        return dataId;
    }

    public void setDataId(Long dataId) {
        this.dataId = dataId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}