package com.ixinhoo.papers.dto.count;

import java.util.List;

/**
 * 会员趋势--折线图DTO
 *
 * @author 448778074@qq.com (cici)
 */
public class UserTendencySumChartLineDto implements java.io.Serializable{
    private List<TendencyDaySumDto> day;//日
    private List<TendencyDaySumDto> week;//周
    private List<TendencyDaySumDto> month;//月

    public List<TendencyDaySumDto> getDay() {
        return day;
    }

    public void setDay(List<TendencyDaySumDto> day) {
        this.day = day;
    }

    public List<TendencyDaySumDto> getWeek() {
        return week;
    }

    public void setWeek(List<TendencyDaySumDto> week) {
        this.week = week;
    }

    public List<TendencyDaySumDto> getMonth() {
        return month;
    }

    public void setMonth(List<TendencyDaySumDto> month) {
        this.month = month;
    }
}
