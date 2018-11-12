package com.ixinhoo.papers.entity.authority;

import com.chunecai.crumbs.api.entity.AuditEntity;
import tk.mybatis.mapper.annotation.NameStyle;
import tk.mybatis.mapper.code.Style;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * 管理员-后台用户-操作记录
 *
 * @author cici
 */
@Table(name = "system_user_operate")
@NameStyle(Style.camelhumpAndLowercase)
public class SystemUserOperate extends AuditEntity {
    @Column
    private Long userId;//用户id
    @Column
    private String userName;//用户名称
    @Column
    private String ip;//ip地址
    @Column
    private Long time;//操作时间
    @Column
    private Integer type;//操作类型；1登录、2登出、3授权、4数据修改
    @Column
    private String content;//操作内容
    @Column
    private String remark;//描述
    @Column
    private String entityName;//操作的实体
    @Column
    private String methodName;//执行的方法名称
    @Column
    private String classLocation;//方法类路径
    @Column
    private String exception;//异常

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getClassLocation() {
        return classLocation;
    }

    public void setClassLocation(String classLocation) {
        this.classLocation = classLocation;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}