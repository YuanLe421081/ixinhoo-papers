package com.ixinhoo.papers.dto.base;

import java.util.List;

/**
 * 章节节点信息-含子节点;
 *
 * @author cici
 */
public class ChapterListTreeDto implements java.io.Serializable{
    private Long id;//主键id
    private String name;//名称
    private Long supId;//父级id
    private List<ChapterListTreeDto> children;

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

    public Long getSupId() {
        return supId;
    }

    public void setSupId(Long supId) {
        this.supId = supId;
    }

    public List<ChapterListTreeDto> getChildren() {
        return children;
    }

    public void setChildren(List<ChapterListTreeDto> children) {
        this.children = children;
    }
}