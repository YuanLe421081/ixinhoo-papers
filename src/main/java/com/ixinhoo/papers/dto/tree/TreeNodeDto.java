package com.ixinhoo.papers.dto.tree;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * TreeNodeDto
 *
 * @author 448778074@qq.com (cici)
 */
public class TreeNodeDto implements java.io.Serializable{
    //树节点ID
    private Long code;
    //树节点名称
    private String name;
    //父节点ID
    private Long parent;
    //节点在树中的排序号
    private int sort;
    //节点所在的层级
    private int level;
    private TreeNodeDto parentNode;//父级节点
    //当前节点的二子节点
    private List<TreeNodeDto> list = Lists.newArrayList();
    //当前节点的子孙节点
    private List<TreeNodeDto> allChildren = Lists.newArrayList();

    public TreeNodeDto() {
    }

    public TreeNodeDto(TreeNodeDto obj) {
        this.code = obj.getCode();
        this.name = obj.getName();
        this.parent = obj.getParent();
        this.sort = obj.getSort();
    }

    public void addChild(TreeNodeDto treeNode) {
        this.list.add(treeNode);
    }

    public void removeChild(TreeNodeDto treeNode) {
        this.list.remove(treeNode);
    }

    public TreeNodeDto getParentNode() {
        return parentNode;
    }

    public void setParentNode(TreeNodeDto parentNode) {
        this.parentNode = parentNode;
    }

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getParent() {
        return parent;
    }

    public void setParent(Long parent) {
        this.parent = parent;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public List<TreeNodeDto> getList() {
        return list;
    }

    public void setList(List<TreeNodeDto> list) {
        this.list = list;
    }

    public void setAllChildren(List<TreeNodeDto> allChildren) {
        this.allChildren = allChildren;
    }

    public List<TreeNodeDto> getAllChildren() {
        if (this.allChildren.isEmpty()) {
            for (TreeNodeDto treeNode : this.list) {
                this.allChildren.add(treeNode);
                this.allChildren.addAll(treeNode.getAllChildren());
            }
        }
        return this.allChildren;
    }
}
