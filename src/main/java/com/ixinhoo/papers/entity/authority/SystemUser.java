package com.ixinhoo.papers.entity.authority;

import com.chunecai.crumbs.api.entity.AuditEntity;
import tk.mybatis.mapper.annotation.NameStyle;
import tk.mybatis.mapper.code.Style;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * 管理员-后台用户
 * normal,                     //原值
 * camelhump,                  //驼峰转下划线
 * uppercase,                  //转换为大写
 * lowercase,                  //转换为小写
 * camelhumpAndUppercase,      //驼峰转下划线大写形式
 * camelhumpAndLowercase,      //驼峰转下划线小写形式
 *
 * @author cici
 */
@Table(name = "system_user")
@NameStyle(Style.camelhumpAndLowercase)
public class SystemUser extends AuditEntity {
    @Column
    private String loginName;//登陆账号
    @Column
    private String salt;//秘钥
    @Column
    private String password;//密码
    @Column
    private Integer status;//状态，1可用、2禁用
    @Column
    private String name;//名称


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}