package com.ixinhoo.papers.entity.user;

import com.chunecai.crumbs.api.entity.AuditEntity;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * 用户购物车
 *
 * @author cici
 */
@Table(name = "shopping_cart")
public class ShoppingCart extends AuditEntity {
    @Column
    private Long userId;//用户id
    @Column
    private Integer dataType;//商品类型（1-资源、2-试卷-预留)
    @Column
    private Long dataId;//数据id
    @Column
    private Long time;//加入时间

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

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }
}