package com.ixinhoo.papers.entity.authority;

import com.chunecai.crumbs.api.entity.AuditEntity;
import tk.mybatis.mapper.annotation.NameStyle;
import tk.mybatis.mapper.code.Style;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * 后台菜单
 *
 * @author cici
 */
@Table(name = "background_menu")
@NameStyle(Style.camelhumpAndLowercase)
public class BackgroundMenu extends AuditEntity {
    @Column
    private String name;//名称
    @Column
    private String code;//标识
    @Column
    private String remark;//描述

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}