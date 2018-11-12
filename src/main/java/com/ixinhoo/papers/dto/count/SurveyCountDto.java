package com.ixinhoo.papers.dto.count;

/**
 * 会员概况DTO
 * @author 448778074@qq.com (cici)
 */
public class SurveyCountDto implements java.io.Serializable{
    private Integer newUser;//新增注册会员（今日）
    private Integer activeUser;//活跃会员（今日）
    private Integer newPayUser;//新增付费会员（今日）
    private Integer sumUser;//累计会员
    private Integer downloadResource;//下载量（今日）
    private Integer templatePaper;//组卷量（今日）
    private Double payMoney;//支付金额（今日）
//    private Double payCoin;//支付备课币（今日）
    private Double fillMoney;//充值金额（今日）

    public Integer getNewUser() {
        return newUser;
    }

    public void setNewUser(Integer newUser) {
        this.newUser = newUser;
    }

    public Integer getActiveUser() {
        return activeUser;
    }

    public void setActiveUser(Integer activeUser) {
        this.activeUser = activeUser;
    }

    public Integer getNewPayUser() {
        return newPayUser;
    }

    public void setNewPayUser(Integer newPayUser) {
        this.newPayUser = newPayUser;
    }

    public Integer getSumUser() {
        return sumUser;
    }

    public void setSumUser(Integer sumUser) {
        this.sumUser = sumUser;
    }

    public Integer getDownloadResource() {
        return downloadResource;
    }

    public void setDownloadResource(Integer downloadResource) {
        this.downloadResource = downloadResource;
    }

    public Integer getTemplatePaper() {
        return templatePaper;
    }

    public void setTemplatePaper(Integer templatePaper) {
        this.templatePaper = templatePaper;
    }

    public Double getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(Double payMoney) {
        this.payMoney = payMoney;
    }
    //    public Double getPayCoin() {
//        return payCoin;
//    }
//
//    public void setPayCoin(Double payCoin) {
//        this.payCoin = payCoin;
//    }

    public Double getFillMoney() {
        return fillMoney;
    }

    public void setFillMoney(Double fillMoney) {
        this.fillMoney = fillMoney;
    }
}
