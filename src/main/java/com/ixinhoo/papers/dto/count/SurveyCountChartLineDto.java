package com.ixinhoo.papers.dto.count;

import java.util.List;

/**
 * 会员概况--折线图DTO
 *
 * @author 448778074@qq.com (cici)
 */
public class SurveyCountChartLineDto implements java.io.Serializable{
    private List<List> today;//今日
    private List<List> yesterday;//昨日
    private List<List> seven;//7天前
    private List<List> thirty;//30天前

    public List<List> getToday() {
        return today;
    }

    public void setToday(List<List> today) {
        this.today = today;
    }

    public List<List> getYesterday() {
        return yesterday;
    }

    public void setYesterday(List<List> yesterday) {
        this.yesterday = yesterday;
    }

    public List<List> getSeven() {
        return seven;
    }

    public void setSeven(List<List> seven) {
        this.seven = seven;
    }

    public List<List> getThirty() {
        return thirty;
    }

    public void setThirty(List<List> thirty) {
        this.thirty = thirty;
    }
}
