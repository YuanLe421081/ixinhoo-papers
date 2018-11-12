package com.ixinhoo.papers.entity.common;

import com.chunecai.crumbs.api.entity.AuditEntity;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * 公司信息
 *
 * @author cici
 */
@Table(name = "company_introduce")
public class CompanyIntroduce extends AuditEntity {
    @Column
    private String name;//名称
    @Column
    private String address;//地址
    @Column
    private String phone;//电话
    @Column
    private String longitude;//经度
    @Column
    private String latitude;//纬度
    @Column
    private String qq;//联系方式-qq
    @Column
    private String email;//邮箱
    @Column
    private String content;//内容

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}