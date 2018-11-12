package com.ixinhoo.papers.dto.user;

/**
 * 预支付订单状态查询
 *
 * @author 448778074@qq.com (cici)
 */
public class UserOrderPreNumStatusDto implements java.io.Serializable{
    private String preNum;//预支付订单（用于查询）
    private Boolean preStatus;//预支付订单状态

    public String getPreNum() {
        return preNum;
    }

    public void setPreNum(String preNum) {
        this.preNum = preNum;
    }

    public Boolean getPreStatus() {
        return preStatus;
    }

    public void setPreStatus(Boolean preStatus) {
        this.preStatus = preStatus;
    }
}
