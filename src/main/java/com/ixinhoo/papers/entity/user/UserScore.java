package com.ixinhoo.papers.entity.user;

import com.chunecai.crumbs.api.entity.AuditEntity;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * 用户评分记录
 *
 * @author cici
 */
@Table(name = "user_score")
public class UserScore extends AuditEntity {
    @Column
    private Long userId;//用户id
    @Column
    private Integer dataType;//数据类型（1-资源、2-试卷-预留)
    @Column
    private Long dataId;//数据id
    @Column
    private Integer score;//分数
    @Column
    private Long time;//评分时间

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getDataType() {
        return dataType;
    }

    public void setDataType(Integer dataType) {
        this.dataType = dataType;
    }

    public Long getDataId() {
        return dataId;
    }

    public void setDataId(Long dataId) {
        this.dataId = dataId;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }
}