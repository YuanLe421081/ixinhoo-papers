package com.ixinhoo.papers.dto.count;

import com.google.common.collect.Maps;
import com.ixinhoo.papers.entity.base.Area;
import com.ixinhoo.papers.entity.base.Subjects;

import java.util.List;
import java.util.Map;

/**
 * 资料统计属性--DTO
 *
 * @author 448778074@qq.com (cici)
 */
public class ResourceCountDto implements java.io.Serializable{
    private Integer today;//今日
    private Integer month;//本月
    private Integer count;//总数
    private Map<Integer, Integer> difficultMap = Maps.newHashMap();//难易程度分布
    private Map<String, Integer> typeMap = Maps.newHashMap();//类型分布
    private Map<Integer, Integer> stageMap = Maps.newHashMap();//学段
    private Map<Integer, Map<Integer, Integer>> gradeMap = Maps.newHashMap();//年级
    private Map<Integer, Map<Long, Integer>> subjectMap = Maps.newHashMap();//Long学科id
    private Map<Long, Integer> areaMap = Maps.newHashMap();//Long 省份id
    private List<Area> areas;//所有省份
    private List<Subjects> subjectss;//所有学科

    public Integer getToday() {
        return today;
    }

    public void setToday(Integer today) {
        this.today = today;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Map<Integer, Integer> getDifficultMap() {
        return difficultMap;
    }

    public void setDifficultMap(Map<Integer, Integer> difficultMap) {
        this.difficultMap = difficultMap;
    }

    public Map<String, Integer> getTypeMap() {
        return typeMap;
    }

    public void setTypeMap(Map<String, Integer> typeMap) {
        this.typeMap = typeMap;
    }

    public Map<Integer, Integer> getStageMap() {
        return stageMap;
    }

    public void setStageMap(Map<Integer, Integer> stageMap) {
        this.stageMap = stageMap;
    }

    public Map<Integer, Map<Integer, Integer>> getGradeMap() {
        return gradeMap;
    }

    public void setGradeMap(Map<Integer, Map<Integer, Integer>> gradeMap) {
        this.gradeMap = gradeMap;
    }

    public Map<Integer, Map<Long, Integer>> getSubjectMap() {
        return subjectMap;
    }

    public void setSubjectMap(Map<Integer, Map<Long, Integer>> subjectMap) {
        this.subjectMap = subjectMap;
    }

    public Map<Long, Integer> getAreaMap() {
        return areaMap;
    }

    public void setAreaMap(Map<Long, Integer> areaMap) {
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
}
