package com.ixinhoo.papers.dto.papers;

import com.ixinhoo.papers.dto.common.CommonIdNameAndNumDto;

import java.util.List;

/**
 * 试卷分析--DTO
 *
 * @author 448778074@qq.com (cici)
 */
public class PaperAnalysisDto implements java.io.Serializable{
    private Long id;//主键
    private String title;//标题
    private List<CommonIdNameAndNumDto> difficult;//难度分布
    private List<CommonIdNameAndNumDto> type;//题量分布
    private List<CommonIdNameAndNumDto> knowledge;//知识点分布

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<CommonIdNameAndNumDto> getDifficult() {
        return difficult;
    }

    public void setDifficult(List<CommonIdNameAndNumDto> difficult) {
        this.difficult = difficult;
    }

    public List<CommonIdNameAndNumDto> getType() {
        return type;
    }

    public void setType(List<CommonIdNameAndNumDto> type) {
        this.type = type;
    }

    public List<CommonIdNameAndNumDto> getKnowledge() {
        return knowledge;
    }

    public void setKnowledge(List<CommonIdNameAndNumDto> knowledge) {
        this.knowledge = knowledge;
    }
}
