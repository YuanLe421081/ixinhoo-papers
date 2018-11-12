package com.ixinhoo.papers.entity.user;

import com.chunecai.crumbs.api.entity.AuditEntity;

import javax.persistence.Table;

/**
 * 用户扩展表，id与user一致
 *
 * @author cici
 */
@Table(name = "user_extend")
public class UserExtend extends AuditEntity {
    private Integer signNum;//签到连续天数
    private Integer signSum;//签到总共天数
    private Integer integral;//用户积分
    private Integer coin;//备课币

    public Integer getSignNum() {
        return signNum;
    }

    public void setSignNum(Integer signNum) {
        this.signNum = signNum;
    }

    public Integer getSignSum() {
        return signSum;
    }

    public void setSignSum(Integer signSum) {
        this.signSum = signSum;
    }

    public Integer getIntegral() {
        return integral;
    }

    public void setIntegral(Integer integral) {
        this.integral = integral;
    }

    public Integer getCoin() {
        return coin;
    }

    public void setCoin(Integer coin) {
        this.coin = coin;
    }
}