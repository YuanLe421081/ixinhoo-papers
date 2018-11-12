package com.ixinhoo.papers.dto.tree;

import java.util.List;

/**
 * 树形节点DTO
 *
 * @author cici
 */
public class CommonTreeDto implements java.io.Serializable{
    private String name;//名称
    private Long code;//id
    private Long parent;//父节点id
    private List<CommonTreeDto> list;//子节点

    public Long getParent() {
        return parent;
    }

    public void setParent(Long parent) {
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public List<CommonTreeDto> getList() {
        return list;
    }

    public void setList(List<CommonTreeDto> list) {
        this.list = list;
    }
}