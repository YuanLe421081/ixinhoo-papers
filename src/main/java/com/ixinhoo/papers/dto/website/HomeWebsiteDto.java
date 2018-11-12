package com.ixinhoo.papers.dto.website;

/**
 * 首页统计数据DTO
 *
 * @author cici
 */
public class HomeWebsiteDto implements java.io.Serializable{
    private Integer documentNum;//总资料数
    private Integer monthDocumentNum;//本月更新
    private Integer todayDocumentNum;//今日更新
    private Integer userNum;//总注册会员
    private Integer todayUserNum;//今日注册

    public Integer getDocumentNum() {
        return documentNum;
    }

    public void setDocumentNum(Integer documentNum) {
        this.documentNum = documentNum;
    }

    public Integer getMonthDocumentNum() {
        return monthDocumentNum;
    }

    public void setMonthDocumentNum(Integer monthDocumentNum) {
        this.monthDocumentNum = monthDocumentNum;
    }

    public Integer getTodayDocumentNum() {
        return todayDocumentNum;
    }

    public void setTodayDocumentNum(Integer todayDocumentNum) {
        this.todayDocumentNum = todayDocumentNum;
    }

    public Integer getUserNum() {
        return userNum;
    }

    public void setUserNum(Integer userNum) {
        this.userNum = userNum;
    }

    public Integer getTodayUserNum() {
        return todayUserNum;
    }

    public void setTodayUserNum(Integer todayUserNum) {
        this.todayUserNum = todayUserNum;
    }
}