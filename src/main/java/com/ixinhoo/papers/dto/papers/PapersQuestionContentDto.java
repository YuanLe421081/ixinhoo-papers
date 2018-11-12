package com.ixinhoo.papers.dto.papers;

import com.ixinhoo.papers.dto.common.CommonIdAndNameDto;

import java.util.List;

/**
 * 试题题目问题及选型
 *
 * @author 448778074@qq.com (cici)
 */
public class PapersQuestionContentDto implements java.io.Serializable{
    private Long id;//试题id
    private String content;//试题内容
    private String options;//选项内容
    private Boolean isCollection = false;//是否收藏
    private Integer difficult;//题目难度值
    private List<CommonIdAndNameDto> knowledge;//知识点

    public List<CommonIdAndNameDto> getKnowledge() {
        return knowledge;
    }

    public void setKnowledge(List<CommonIdAndNameDto> knowledge) {
        this.knowledge = knowledge;
    }

    public Integer getDifficult() {
        return difficult;
    }

    public void setDifficult(Integer difficult) {
        this.difficult = difficult;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public Boolean getCollection() {
        return isCollection;
    }

    public void setCollection(Boolean collection) {
        isCollection = collection;
    }
}
