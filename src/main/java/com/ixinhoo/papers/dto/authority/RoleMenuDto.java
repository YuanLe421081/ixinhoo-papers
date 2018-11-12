package com.ixinhoo.papers.dto.authority;


import java.util.List;

/**
 * 角色菜单dto
 *
 * @author 448778074@qq.com (cici)
 */
public class RoleMenuDto implements java.io.Serializable {
    private Long id;
    private String name;//名称
    private String code;//标识
    private String remark;//描述
    private List<Long> menuIds;//角色包含的菜单id集合

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public List<Long> getMenuIds() {
        return menuIds;
    }

    public void setMenuIds(List<Long> menuIds) {
        this.menuIds = menuIds;
    }
}
