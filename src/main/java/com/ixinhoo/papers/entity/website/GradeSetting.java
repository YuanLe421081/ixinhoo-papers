package com.ixinhoo.papers.entity.website;

import com.chunecai.crumbs.api.entity.AuditEntity;

import javax.persistence.Table;

/**
 * 等级设置；--初始化数据--内置7个等级。
 *
 * @author cici
 */
@Table(name = "grade_setting")
public class GradeSetting extends AuditEntity {
    private String code;//标识
    private String name;//名称
    private Integer beginIntegral;//开始积分
    private Integer endIntegral;//结束积分
    private String remark;//备注描述
    private String icon;//图标地址

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getBeginIntegral() {
        return beginIntegral;
    }

    public void setBeginIntegral(Integer beginIntegral) {
        this.beginIntegral = beginIntegral;
    }

    public Integer getEndIntegral() {
        return endIntegral;
    }

    public void setEndIntegral(Integer endIntegral) {
        this.endIntegral = endIntegral;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}