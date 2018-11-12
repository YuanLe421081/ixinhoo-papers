package com.ixinhoo.papers.dto.user;

/**
 * 用户注册dto
 *
 * @author 448778074@qq.com (cici)
 */
public class UserRegisterDto implements java.io.Serializable{
    private String phone;//手机号
    private String code;//验证码
    private String nickName;//昵称
    private String image;//头像
    private String qq;//qq号
    private String wechat;//微信号
    private String weibo;//微博号
    private String password;//密码
    private Long inviteId;//邀请人id-预留

    public Long getInviteId() {
        return inviteId;
    }

    public void setInviteId(Long inviteId) {
        this.inviteId = inviteId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public String getWeibo() {
        return weibo;
    }

    public void setWeibo(String weibo) {
        this.weibo = weibo;
    }
}
