package com.ixinhoo.papers.dto.freemarker;

import java.util.List;

/**
 * 试卷下载dto;--问题类型（分组分类）
 *
 * @author 448778074@qq.com (cici)
 */
public class PaperDownloadQuestionTypeDto implements java.io.Serializable{
    private String sort;//排序、中文
    private String title;//标题
    private Integer questionNum;//题目总数
    private Integer score;//总分
    private List<PaperDownloadQuestionDto> questions;//问题集合

    public Integer getQuestionNum() {
        return questionNum;
    }

    public void setQuestionNum(Integer questionNum) {
        this.questionNum = questionNum;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<PaperDownloadQuestionDto> getQuestions() {
        return questions;
    }

    public void setQuestions(List<PaperDownloadQuestionDto> questions) {
        this.questions = questions;
    }
}
