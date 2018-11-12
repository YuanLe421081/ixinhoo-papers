package com.ixinhoo.papers.dto.website;

import java.util.List;

/**
 * 促销活动DTO
 *
 * @author cici
 */
public class PromotionActivityDto implements java.io.Serializable{
    private Long id;//主键
    private String name;//名称
    private Integer type;//类型
    private Long beginTime;//开始时间
    private Long endTime;//结束时间
    private Integer status;//状态
    private Integer sendMessage;//是否发送站内信
    private Long noticeId;//站内信id
    private String noticeTitle;//标题
    private String noticeContent;//内容
    private Double discount;//折扣
    private List<Integer> stage;//学段
    private List<Integer> grade;//年级
    private List<Long> subject;//学科
    private List<Long> province;//省份


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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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

    public Integer getSendMessage() {
        return sendMessage;
    }

    public void setSendMessage(Integer sendMessage) {
        this.sendMessage = sendMessage;
    }

    public Long getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(Long noticeId) {
        this.noticeId = noticeId;
    }

    public String getNoticeTitle() {
        return noticeTitle;
    }

    public void setNoticeTitle(String noticeTitle) {
        this.noticeTitle = noticeTitle;
    }

    public String getNoticeContent() {
        return noticeContent;
    }

    public void setNoticeContent(String noticeContent) {
        this.noticeContent = noticeContent;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public List<Integer> getStage() {
        return stage;
    }

    public void setStage(List<Integer> stage) {
        this.stage = stage;
    }

    public List<Integer> getGrade() {
        return grade;
    }

    public void setGrade(List<Integer> grade) {
        this.grade = grade;
    }

    public List<Long> getSubject() {
        return subject;
    }

    public void setSubject(List<Long> subject) {
        this.subject = subject;
    }

    public List<Long> getProvince() {
        return province;
    }

    public void setProvince(List<Long> province) {
        this.province = province;
    }
}