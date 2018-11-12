package com.ixinhoo.papers.entity.authority;

import com.chunecai.crumbs.api.entity.AuditEntity;
import tk.mybatis.mapper.annotation.NameStyle;
import tk.mybatis.mapper.code.Style;

import javax.persistence.*;
import java.util.Set;

/**
 * 角色
 *
 * @author cici
 */
@Table(name = "role")
@NameStyle(Style.camelhumpAndLowercase)
public class Role extends AuditEntity {
    @Column
    private String name;//名称
    @Column
    private String code;//标识
    @Column
    private String remark;//描述
//    private Set<BackgroundMenu> menu;//菜单

    // 多对多定义
//    @ManyToMany
//    @JoinTable(name = "role_menu", joinColumns = {@JoinColumn(name = "role_id")}, inverseJoinColumns = {@JoinColumn(name = "menu_id")})
//    public Set<BackgroundMenu> getMenu() {
//        return menu;
//    }
//
//    public void setMenu(Set<BackgroundMenu> menu) {
//        this.menu = menu;
//    }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

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