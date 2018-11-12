package com.ixinhoo.papers.entity.website;

import com.chunecai.crumbs.api.entity.AuditEntity;

import javax.persistence.Table;

/**
 * 促销活动
 *
 * @author cici
 */
@Table(name = "promotion_activity")
public class PromotionActivity extends AuditEntity {
    private String name;//名称
    private Integer type;//类型、1--下载优惠、2--充值优惠
    private Long beginTime;//开始时间
    private Long endTime;//结束时间
    private Integer status;//状态，1--启用、2--禁用暂停
    private Integer sendMessage;//是否发送站内信，1--发送、2--不发送
    private Long noticeId;//消息通知id
    private Double discount;//活动折扣

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

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }
}