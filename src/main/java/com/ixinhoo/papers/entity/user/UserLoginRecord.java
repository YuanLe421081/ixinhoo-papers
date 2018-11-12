package com.ixinhoo.papers.entity.user;

import com.chunecai.crumbs.api.entity.AuditEntity;

import javax.persistence.Table;

/**
 * 用户登陆记录
 *
 * @author cici
 */
@Table(name = "user_login_record")
public class UserLoginRecord extends AuditEntity {
    private Long userId;//用户id
    private String account;//登录账号
    private Integer platform;//登录平台
    private String ip;//ip地址
    private Long time;//登陆时间

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Integer getPlatform() {
        return platform;
    }

    public void setPlatform(Integer platform) {
        this.platform = platform;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }
}