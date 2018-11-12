package com.ixinhoo.papers.dto.count;

/**
 * 分组每天、每周、每月--DTO
 *
 * @author 448778074@qq.com (cici)
 */
public class TendencyDaySumDto implements java.io.Serializable {
    private String day;//日期
    private Double count;//总数

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public Double getCount() {
        return count;
    }

    public void setCount(Double count) {
        this.count = count;
    }
}
