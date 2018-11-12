package com.ixinhoo.papers.dto.user;

import java.util.List;

/**
 * 用户订单请求DTO
 *
 * @author cici
 */
public class UserOrderReqDto implements java.io.Serializable{
    private Long userId;//用户id
    private Integer p;//当前页
    private Integer s;//页大小
    private Long beginTime;//开始时间
    private Long endTime;//结束时间
    private List<Integer> type;//订单类型，1-充值、2-下载、3-组卷

    public List<Integer> getType() {
        return type;
    }

    public void setType(List<Integer> type) {
        this.type = type;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getP() {
        return p;
    }

    public void setP(Integer p) {
        this.p = p;
    }

    public Integer getS() {
        return s;
    }

    public void setS(Integer s) {
        this.s = s;
    }

    public Long getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Long beginTime) {
        this.beginTime = beginTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }
}