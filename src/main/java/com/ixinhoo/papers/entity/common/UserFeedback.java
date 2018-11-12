package com.ixinhoo.papers.entity.common;

import com.chunecai.crumbs.api.entity.AuditEntity;
import tk.mybatis.mapper.annotation.NameStyle;
import tk.mybatis.mapper.code.Style;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * 用户意见反馈
 *
 * @author cici
 */
@Table(name = "user_feedback")
@NameStyle(Style.camelhumpAndLowercase)
public class UserFeedback extends AuditEntity {
    @Column
    private Long userId;//用户主键
    @Column
    private String userName;//用户姓名
    @Column
    private Long time;//反馈时间
    @Column
    private String title;//标题
    @Column
    private String content;//内容
    @Column
    private String contact;//联系方式

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
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

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}