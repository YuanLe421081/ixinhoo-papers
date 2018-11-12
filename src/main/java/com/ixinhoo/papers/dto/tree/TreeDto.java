package com.ixinhoo.papers.dto.tree;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * 属性节点DTO
 */
public class TreeDto implements java.io.Serializable{
    private HashMap<Long, TreeNodeDto> treeNodesMap = Maps.newHashMap();
    private List<TreeNodeDto> treeNodesList = Lists.newArrayList();

    public TreeDto(List<TreeNodeDto> list) {
        initTreeNodeMap(list);
        initTreeNodeList();
    }

    private void initTreeNodeMap(List<TreeNodeDto> list) {
        for (TreeNodeDto item : list) {
            treeNodesMap.put(item.getCode(), item);
        }

        Iterator<TreeNodeDto> iter = treeNodesMap.values().iterator();
        TreeNodeDto parentTreeNode = null;
        while (iter.hasNext()) {
            TreeNodeDto treeNode = iter.next();
            if (treeNode.getParent() == null || treeNode.getParent() == 0L) {
                continue;
            }

            parentTreeNode = treeNodesMap.get(treeNode.getParent());
            if (parentTreeNode != null) {
                treeNode.setParentNode(parentTreeNode);
                parentTreeNode.addChild(treeNode);
            }
        }
    }

    private void initTreeNodeList() {
        if (treeNodesList.size() > 0) {
            return;
        }
        if (treeNodesMap.size() == 0) {
            return;
        }
        Iterator<TreeNodeDto> iter = treeNodesMap.values().iterator();
        TreeNodeDto treeNode = null;
        while (iter.hasNext()) {
            treeNode = iter.next();
            if (treeNode.getParentNode() == null) {
                this.treeNodesList.add(treeNode);
                this.treeNodesList.addAll(treeNode.getAllChildren());
            }
        }
    }


    public List<TreeNodeDto> getTree() {
        return this.treeNodesList;
    }


    public List<TreeNodeDto> getRoot() {
        List<TreeNodeDto> rootList = Lists.newArrayList();
        if (this.treeNodesList.size() > 0) {
            for (TreeNodeDto node : treeNodesList) {
                if (node.getParentNode() == null){
                    rootList.add(node);
                }
            }
        }
        return rootList;
    }


    public TreeNodeDto getTreeNode(String nodeId) {
        return this.treeNodesMap.get(nodeId);
    }

}