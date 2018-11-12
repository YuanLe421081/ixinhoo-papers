package com.ixinhoo.papers.dto.papers;

import java.util.List;

/**
 * 根据章节、知识点生成试卷；组卷
 *
 * @author 448778074@qq.com (cici)
 */
public class ChapterToPaperTypeDto implements java.io.Serializable{
    private Long id;//类型id
    private Integer num;//类型出题数目
    private List<Long> contactIds;//关联的章节id或者知识点集合
    private Integer difficult;//难度

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public List<Long> getContactIds() {
        return contactIds;
    }

    public void setContactIds(List<Long> contactIds) {
        this.contactIds = contactIds;
    }

    public Integer getDifficult() {
        return difficult;
    }

    public void setDifficult(Integer difficult) {
        this.difficult = difficult;
    }
}
