package com.ixinhoo.papers.entity.user;


import com.chunecai.crumbs.api.entity.AuditEntity;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * 用户下载记录
 *
 * @author cici
 */
@Table(name = "user_download")
public class UserDownload extends AuditEntity {
    @Column
    private Long userId;//用户主键id
    @Column
    private Integer type;//类型【课件、教案、学案、视频、试题、其他】
    @Column
    private Long dataId;//数据id
    @Column
    private Long time;//下载时间
    @Column
    private Long versionId;//版本id
    @Column
    private String versionName;//版本名称

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

    public Long getVersionId() {
        return versionId;
    }

    public void setVersionId(Long versionId) {
        this.versionId = versionId;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }
}