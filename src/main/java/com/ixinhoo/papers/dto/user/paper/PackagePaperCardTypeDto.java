package com.ixinhoo.papers.dto.user.paper;

import java.util.List;

/**
 * 下载答题卡-类型DTO
 *
 * @author 448778074@qq.com (cici)
 */
public class PackagePaperCardTypeDto implements java.io.Serializable{
    private Long id;//类型id
    private List<Long> question;//问题列表

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Long> getQuestion() {
        return question;
    }

    public void setQuestion(List<Long> question) {
        this.question = question;
    }
}
