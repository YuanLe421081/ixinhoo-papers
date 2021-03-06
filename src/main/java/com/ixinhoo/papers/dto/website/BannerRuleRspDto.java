package com.ixinhoo.papers.dto.website;

/**
 * 广告规则响应DTO
 *
 * @author cici
 */
public class BannerRuleRspDto implements java.io.Serializable{
    private Long id;//主键
    private String name;//名称
    private Long bannerId;//广告id
    private String icon;//图标地址
    private String link;//链接地址
    private Integer defaultShow;//默认显示
    private Long beginTime;//开始时间
    private Long endTime;//结束时间
    private Integer status;//状态
    private String stage;//学段
    private String grade;//年级
    private String subject;//学科
    private String province;//省份
    private String teachAge;//教学年龄


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

    public Long getBannerId() {
        return bannerId;
    }

    public void setBannerId(Long bannerId) {
        this.bannerId = bannerId;
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

    public Integer getDefaultShow() {
        return defaultShow;
    }

    public void setDefaultShow(Integer defaultShow) {
        this.defaultShow = defaultShow;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getTeachAge() {
        return teachAge;
    }

    public void setTeachAge(String teachAge) {
        this.teachAge = teachAge;
    }
}