package com.ixinhoo.papers.entity.website;

import com.chunecai.crumbs.api.entity.AuditEntity;

import javax.persistence.Table;

/**
 * 积分规则--初始化数据--内置，只是积分可以修改
 *
 * @author cici
 */
@Table(name = "integral_rule")
public class IntegralRule extends AuditEntity {
    private Integer onceIntegral;//单次积分
    private Integer maxIntegral;//每日/七天奖励最多积分
    private Integer type;//类型；1-签到、2-消费1元、3-上传资料、4-分享、5-资料评分

    public Integer getOnceIntegral() {
        return onceIntegral;
    }

    public void setOnceIntegral(Integer onceIntegral) {
        this.onceIntegral = onceIntegral;
    }

    public Integer getMaxIntegral() {
        return maxIntegral;
    }

    public void setMaxIntegral(Integer maxIntegral) {
        this.maxIntegral = maxIntegral;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}