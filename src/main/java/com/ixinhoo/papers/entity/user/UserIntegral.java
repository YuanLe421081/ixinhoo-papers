package com.ixinhoo.papers.entity.user;


import com.chunecai.crumbs.api.entity.AuditEntity;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * 用户积分&备课币记录
 *
 * @author cici
 */
@Table(name = "user_integral")
public class UserIntegral extends AuditEntity {
    @Column
    private Long userId;//用户主键id
    @Column
    private Integer type;//类型、1-增加、2减少
    @Column
    private Integer num;//数目
    @Column
    private Integer source;//来源（充值、消费、拉新-预留、管理员赠送-预留；付费、签到）
    @Column
    private Long operatorId;//操作者id
    @Column
    private Integer operatorType;//操作者类型、1-用户、2-管理员
    @Column
    private Long time;//获取时间
    @Column
    private Integer recordType;//记录类型、1-备课币、积分

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public Long getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
    }

    public Integer getOperatorType() {
        return operatorType;
    }

    public void setOperatorType(Integer operatorType) {
        this.operatorType = operatorType;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Integer getRecordType() {
        return recordType;
    }

    public void setRecordType(Integer recordType) {
        this.recordType = recordType;
    }
}