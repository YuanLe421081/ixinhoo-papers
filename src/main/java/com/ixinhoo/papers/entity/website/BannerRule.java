package com.ixinhoo.papers.entity.website;

import com.chunecai.crumbs.api.entity.AuditEntity;

import javax.persistence.Table;

/**
 * 广告规则；
 * 广告banner设置、广告管理下面的广告规则
 *
 * @author cici
 */
@Table(name = "banner_rule")
public class BannerRule extends AuditEntity {

    private Long bannerId;//广告id

    private String name;//名称

    private String icon;//图标地址

    private String link;//链接地址

    private Long beginTime;//开始时间

    private Long endTime;//结束时间

    private Integer status;//状态

    private Integer defaultShow;//默认显示--预留

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getBannerId() {
        return bannerId;
    }

    public void setBannerId(Long bannerId) {
        this.bannerId = bannerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Long getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Long beginTime) {
        this.beginTime = beginTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public Integer getDefaultShow() {
        return defaultShow;
    }

    public void setDefaultShow(Integer defaultShow) {
        this.defaultShow = defaultShow;
    }
}