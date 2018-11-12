package com.ixinhoo.papers.dto.papers;

import java.util.List;

/**
 * 根据章节、知识点生成试卷；组卷；
 * 关联出题：匹配出来的试题包含的知识点（章节），最少有一个在已选的知识点（章节）中，这个方式适用于期末考试、学业考试、升学考试等试卷类型。出题的综合性较强。
 * 精准出题：匹配出来的试题包含的知识点（章节）。都在已选的知识点（章节）中，这个方式保证了组卷的精准性，避免超纲试题的出现，适用于同步类型的试卷。
 *
 * @author 448778074@qq.com (cici)
 */
public class ChapterToPaperDto implements java.io.Serializable{
    private Long userId;//用户主键
    private List<Long> selectedIds;//章节id或知识点集合
    private List<ChapterToPaperTypeDto> types;//类型
    private Integer style;//出题方式，1.关联出题，2.精准出题

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<Long> getSelectedIds() {
        return selectedIds;
    }

    public void setSelectedIds(List<Long> selectedIds) {
        this.selectedIds = selectedIds;
    }

    public List<ChapterToPaperTypeDto> getTypes() {
        return types;
    }

    public void setTypes(List<ChapterToPaperTypeDto> types) {
        this.types = types;
    }

    public Integer getStyle() {
        return style;
    }

    public void setStyle(Integer style) {
        this.style = style;
    }
}
