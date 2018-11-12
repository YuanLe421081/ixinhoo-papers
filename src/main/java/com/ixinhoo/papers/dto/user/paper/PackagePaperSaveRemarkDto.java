package com.ixinhoo.papers.dto.user.paper;

/**
 * 保存用户组卷信息--修改的备注信息
 *
 * @author 448778074@qq.com (cici)
 */
public class PackagePaperSaveRemarkDto implements java.io.Serializable{
    private String secondTitle;//副标题
    private String firstSection;//第一卷名称
    private String secondSection;//第2卷名称
    private String attention;//注意事项
    private Integer minute;//考试时间-分钟
    private Integer score;//考试总分

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
}
