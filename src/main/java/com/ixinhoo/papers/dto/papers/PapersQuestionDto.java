package com.ixinhoo.papers.dto.papers;

import java.util.List;

/**
 * 试卷-含问题DTO
 *
 * @author 448778074@qq.com (cici)
 */
public class PapersQuestionDto implements java.io.Serializable{
    private String title;//试卷标题
    private Long paperId;//试卷的id、如果是从试卷那边跳转的话这个就有值;
    private List<PapersQuestionTypeDto> typeList;//类型集合

    public Long getPaperId() {
        return paperId;
    }

    public void setPaperId(Long paperId) {
        this.paperId = paperId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<PapersQuestionTypeDto> getTypeList() {
        return typeList;
    }

    public void setTypeList(List<PapersQuestionTypeDto> typeList) {
        this.typeList = typeList;
    }
}
