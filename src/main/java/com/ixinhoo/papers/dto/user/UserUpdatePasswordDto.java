package com.ixinhoo.papers.dto.user;

/**
 * 用户信息--个人中心--修改密码DTO
 *
 * @author cici
 */
public class UserUpdatePasswordDto implements java.io.Serializable{
    private Long id;
    private String oldPwd;//旧密码
    private String newPwd;//新密码

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOldPwd() {
        return oldPwd;
    }

    public void setOldPwd(String oldPwd) {
        this.oldPwd = oldPwd;
    }

    public String getNewPwd() {
        return newPwd;
    }

    public void setNewPwd(String newPwd) {
        this.newPwd = newPwd;
    }
}