package com.ixinhoo.papers.entity.user;

import com.chunecai.crumbs.api.entity.AuditEntity;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * 用户收藏
 *
 * @author cici
 */
@Table(name = "user_collection")
public class UserCollection extends AuditEntity {
    @Column
    private Long userId;//用户id
    @Column
    private Integer typeId;//数据所属分类id；（3 = '课件', 8 = '教案', 7 = '试卷', 4 = '学案',11= '资源包',6 = '素材',12 = '视频'）20-试题
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

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }
}