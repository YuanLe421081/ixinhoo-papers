package com.ixinhoo.papers.dto.freemarker;

import java.util.List;

/**
 * 试卷下载dto;
 *	template模板,1密封线，2考试时间，3大题评分区，4分卷注释，5主标题，6考生填写，7注意事项，8分大题，9副标题，10总评分，11分卷，12大题注释
 * @author 448778074@qq.com (cici)
 */
public class PaperDownloadDto implements java.io.Serializable{
    private String title;//标题
    private List<PaperDownloadQuestionTypeDto> types1;//题类&题目--第1卷；没有分卷都选1
    private List<PaperDownloadQuestionTypeDto> types2;//题类&题目--第2卷
    private String secondTitle;//副标题
    private String firstSection="第I卷";//第一卷名称
    private String secondSection="第II卷";//第2卷名称
    private String firstNotes="第一卷注释";//第一卷注释
    private String secondNotes="第二卷注释";//第二卷注释
    private String attention="注意事项";//注意事项
    private Integer minute;//考试时间-分钟
    private Integer score;//考试总分
    private List<Integer> template;//模板数组json
    private List<String> bigQuestionSort;//存放大题排序号,如：一、二、三
    private List<Integer> paperType;//试卷类型

    public List<Integer> getPaperType() {
        return paperType;
    }

    public void setPaperType(List<Integer> paperType) {
        this.paperType = paperType;
    }

    public String getFirstNotes() {
        return firstNotes;
    }

    public void setFirstNotes(String firstNotes) {
        this.firstNotes = firstNotes;
    }

    public String getSecondNotes() {
        return secondNotes;
    }

    public void setSecondNotes(String secondNotes) {
        this.secondNotes = secondNotes;
    }

    public List<String> getBigQuestionSort() {
        return bigQuestionSort;
    }

    public void setBigQuestionSort(List<String> bigQuestionSort) {
        this.bigQuestionSort = bigQuestionSort;
    }

    public List<Integer> getTemplate() {
        return template;
    }

    public void setTemplate(List<Integer> template) {
        this.template = template;
    }

    public List<PaperDownloadQuestionTypeDto> getTypes1() {
        return types1;
    }

    public void setTypes1(List<PaperDownloadQuestionTypeDto> types1) {
        this.types1 = types1;
    }

    public List<PaperDownloadQuestionTypeDto> getTypes2() {
        return types2;
    }

    public void setTypes2(List<PaperDownloadQuestionTypeDto> types2) {
        this.types2 = types2;
    }

    public String getSecondTitle() {
        return secondTitle;
    }

    public void setSecondTitle(String secondTitle) {
        this.secondTitle = secondTitle;
    }

    public String getFirstSection() {
        return firstSection;
    }

    public void setFirstSection(String firstSection) {
        this.firstSection = firstSection;
    }

    public String getSecondSection() {
        return secondSection;
    }

    public void setSecondSection(String secondSection) {
        this.secondSection = secondSection;
    }

    public String getAttention() {
        return attention;
    }

    public void setAttention(String attention) {
        this.attention = attention;
    }

    public Integer getMinute() {
        return minute;
    }

    public void setMinute(Integer minute) {
        this.minute = minute;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


}
