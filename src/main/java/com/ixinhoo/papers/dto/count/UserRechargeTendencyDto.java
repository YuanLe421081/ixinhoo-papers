package com.ixinhoo.papers.dto.count;

import com.google.common.collect.Maps;
import com.ixinhoo.papers.entity.base.Area;
import com.ixinhoo.papers.entity.base.Subjects;

import java.util.List;
import java.util.Map;

/**
 * 支付统计趋势DTO
 *
 * @author 448778074@qq.com (cici)
 */
public class UserRechargeTendencyDto implements java.io.Serializable {
    private UserTendencySumChartLineDto tendency;
    private Map<Integer, Double> payMap = Maps.newHashMap();//支付
    private Map<Integer, Double> typeMap = Maps.newHashMap();//类型分布
    private Map<Integer, Double> difficultMap = Maps.newHashMap();//难易程度分布
    private Map<Integer, Double> teachMap = Maps.newHashMap();//教龄
    private Map<Integer, Double> stageMap = Maps.newHashMap();//学段
    private Map<Integer, Map<Integer, Double>> gradeMap = Maps.newHashMap();//年级-暂无
    private Map<Integer, Map<Long, Double>> subjectMap = Maps.newHashMap();//Long学科id
    private Map<Long, Double> areaMap = Maps.newHashMap();//Long 省份id
    private List<Area> areas;//所有省份
    private List<Subjects> subjectss;//所有学科
    private Double count;//总数

    public Map<Integer, Double> getPayMap() {
        return payMap;
    }

    public void setPayMap(Map<Integer, Double> payMap) {
        this.payMap = payMap;
    }

    public UserTendencySumChartLineDto getTendency() {
        return tendency;
    }

    public void setTendency(UserTendencySumChartLineDto tendency) {
        this.tendency = tendency;
    }

    public Map<Integer, Double> getTypeMap() {
        return typeMap;
    }

    public void setTypeMap(Map<Integer, Double> typeMap) {
        this.typeMap = typeMap;
    }

    public Map<Integer, Double> getDifficultMap() {
        return difficultMap;
    }

    public void setDifficultMap(Map<Integer, Double> difficultMap) {
        this.difficultMap = difficultMap;
    }

    public Map<Integer, Double> getTeachMap() {
        return teachMap;
    }

    public void setTeachMap(Map<Integer, Double> teachMap) {
        this.teachMap = teachMap;
    }

    public Map<Integer, Double> getStageMap() {
        return stageMap;
    }

    public void setStageMap(Map<Integer, Double> stageMap) {
        this.stageMap = stageMap;
    }

    public Map<Integer, Map<Integer, Double>> getGradeMap() {
        return gradeMap;
    }

    public void setGradeMap(Map<Integer, Map<Integer, Double>> gradeMap) {
        this.gradeMap = gradeMap;
    }

    public Map<Integer, Map<Long, Double>> getSubjectMap() {
        return subjectMap;
    }

    public void setSubjectMap(Map<Integer, Map<Long, Double>> subjectMap) {
        this.subjectMap = subjectMap;
    }

    public Map<Long, Double> getAreaMap() {
        return areaMap;
    }

    public void setAreaMap(Map<Long, Double> areaMap) {
        this.areaMap = areaMap;
    }

    public List<Area> getAreas() {
        return areas;
    }

    public void setAreas(List<Area> areas) {
        this.areas = areas;
    }

    public List<Subjects> getSubjectss() {
        return subjectss;
    }

    public void setSubjectss(List<Subjects> subjectss) {
        this.subjectss = subjectss;
    }

    public Double getCount() {
        return count;
    }

    public void setCount(Double count) {
        this.count = count;
    }
}
