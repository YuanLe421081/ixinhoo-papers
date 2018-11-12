package com.ixinhoo.papers.entity.website;

import com.chunecai.crumbs.api.entity.AuditEntity;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * 充值金额设置
 *
 * @author cici
 */
@Table(name = "fill_money_setting")
public class FillMoneySetting extends AuditEntity {
    @Column
    private Double price;//金额
    @Column
    private Integer coin;//所得的备课币
    @Column
    private Integer status;//状态
    @Column
    private Long beginTime;//开始时间
    @Column
    private Long endTime;//结束时间

    public Integer getCoin() {
        return coin;
    }

    public void setCoin(Integer coin) {
        this.coin = coin;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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
}