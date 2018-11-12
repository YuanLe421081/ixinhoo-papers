package com.ixinhoo.papers.dto.count;

import java.util.List;

/**
 * 会员趋势--折线图DTO
 *
 * @author 448778074@qq.com (cici)
 */
public class UserTendencyChartLineDto implements java.io.Serializable{
    private List<TendencyDayCountDto> day;//日
    private List<TendencyDayCountDto> week;//周
    private List<TendencyDayCountDto> month;//月

    public List<TendencyDayCountDto> getDay() {
        return day;
    }

    public void setDay(List<TendencyDayCountDto> day) {
        this.day = day;
    }

    public List<TendencyDayCountDto> getWeek() {
        return week;
    }

    public void setWeek(List<TendencyDayCountDto> week) {
        this.week = week;
    }

    public List<TendencyDayCountDto> getMonth() {
        return month;
    }

    public void setMonth(List<TendencyDayCountDto> month) {
        this.month = month;
    }
}
