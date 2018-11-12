package com.ixinhoo.papers.entity.website;

import com.chunecai.crumbs.api.entity.AuditEntity;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * 备课币设置
 *
 * @author cici
 */
@Table(name = "coin_setting")
public class CoinSetting extends AuditEntity {
    @Column
    private Double price;//价格
    @Column
    private Integer status;//状态
    @Column
    private Long beginTime;//开始时间
    @Column
    private Long endTime;//结束时间
    @Column
    private Integer paperCoin;//下载试卷所需备课币

    public Integer getPaperCoin() {
        return paperCoin;
    }

    public void setPaperCoin(Integer paperCoin) {
        this.paperCoin = paperCoin;
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