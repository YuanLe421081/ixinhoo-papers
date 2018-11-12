package com.ixinhoo.papers.entity.user;

import com.chunecai.crumbs.api.entity.AuditEntity;

import javax.persistence.Table;

/**
 * 用户消息读取状态
 *
 * @author cici
 */
@Table(name = "user_message_read")
public class UserMessageRead extends AuditEntity {
    private Long userId;//用户id
    private Long messageId;//消息id
    private Integer status;//阅读状态,1--已读、2--未读
    private Long time;//阅读时间

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }
}