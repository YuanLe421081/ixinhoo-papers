package com.ixinhoo.papers.entity.website;

import com.chunecai.crumbs.api.entity.AuditEntity;

import javax.persistence.Table;

/**
 * 广告规则下的广告人群；
 *
 * @author cici
 */
@Table(name = "banner_rule_range")
public class BannerRuleRange extends AuditEntity {

    private Long ruleId;//广告规则id
    private String stage;//学段集合
    private String grade;//年级集合
    private String subject;//学科集合
    private String province;//省地区集合
    private String teachAge;//教龄集合

    public Long getRuleId() {
        return ruleId;
    }

    public void setRuleId(Long ruleId) {
        this.ruleId = ruleId;
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