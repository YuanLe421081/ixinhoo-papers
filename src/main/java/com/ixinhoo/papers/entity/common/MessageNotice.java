package com.ixinhoo.papers.entity.common;

import com.chunecai.crumbs.api.entity.AuditEntity;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * 消息通知
 *
 * @author cici
 */
@Table(name = "message_notice")
public class MessageNotice extends AuditEntity {
    @Column
    private String title;//标题
    @Column
    private String content;//内容
    @Column
    private Integer type;//消息类型，1-系统消息、2系统公告、3平台活动
    @Column
    private Integer pushType;//推送类型-1所有，2个人、3角色、
    @Column
    private String dataCode;//数据标识，个人为手机号、角色为角色标识
    @Column
    private Long pushTime;//推送时间

    public String getDataCode() {
        return dataCode;
    }

    public void setDataCode(String dataCode) {
        this.dataCode = dataCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getPushType() {
        return pushType;
    }

    public void setPushType(Integer pushType) {
        this.pushType = pushType;
    }


    public Long getPushTime() {
        return pushTime;
    }

    public void setPushTime(Long pushTime) {
        this.pushTime = pushTime;
    }
}