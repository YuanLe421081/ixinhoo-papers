package com.ixinhoo.papers.dto.user;

/**
 * 用户重置密码dto
 *
 * @author 448778074@qq.com (cici)
 */
public class UserResetPasswordDto implements java.io.Serializable{
    private String phone;//手机号
    private String code;//验证码
    private String password;//密码

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
