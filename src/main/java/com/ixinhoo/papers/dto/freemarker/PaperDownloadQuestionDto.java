package com.ixinhoo.papers.dto.freemarker;

import java.util.List;

/**
 * 试卷下载dto;--问题和答案
 *
 * @author 448778074@qq.com (cici)
 */
public class PaperDownloadQuestionDto implements java.io.Serializable{
    private Integer sort;//排序
    private String title;//标题
    private Integer score;//分数
    private List<String> options;//选项，选择题使用
    private String answer;//答案
    private String explanation;//解析
    private String knowledge;//考点

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public String getKnowledge() {
        return knowledge;
    }

    public void setKnowledge(String knowledge) {
        this.knowledge = knowledge;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getOptions() {
        return options;
    }
}
