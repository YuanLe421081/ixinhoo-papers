package com.ixinhoo.papers.dto.count;

import com.google.common.collect.Maps;
import com.ixinhoo.papers.entity.base.Area;
import com.ixinhoo.papers.entity.base.Subjects;

import java.util.List;
import java.util.Map;

/**
 * 会员属性--DTO
 *
 * @author 448778074@qq.com (cici)
 */
public class UserAttributeDto implements java.io.Serializable{
    private Integer count;//用户总数
    private Map<Integer, Integer> sexMap = Maps.newHashMap();//性别分布
    private Map<Integer, Integer> ageMap = Maps.newHashMap();//年龄
    private Map<Integer, Integer> teachMap = Maps.newHashMap();//教龄
    private Map<Integer, Integer> stageMap = Maps.newHashMap();//学段
    private Map<Integer, Map<Integer, Integer>> gradeMap = Maps.newHashMap();//年级
    private Map<Integer, Map<Long, Integer>> subjectMap = Maps.newHashMap();//Long学科id
    private Map<Long, Integer> areaMap = Maps.newHashMap();//Long 省份id
    private Map<Integer, Integer> payMap = Maps.newHashMap();//消费金额分布
    private List<Area> areas;//所有省份
    private List<Subjects> subjectss;//所有学科

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Map<Integer, Integer> getSexMap() {
        return sexMap;
    }

    public void setSexMap(Map<Integer, Integer> sexMap) {
        this.sexMap = sexMap;
    }

    public Map<Integer, Integer> getAgeMap() {
        return ageMap;
    }

    public void setAgeMap(Map<Integer, Integer> ageMap) {
        this.ageMap = ageMap;
    }

    public Map<Integer, Integer> getTeachMap() {
        return teachMap;
    }

    public void setTeachMap(Map<Integer, Integer> teachMap) {
        this.teachMap = teachMap;
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

    public Map<Integer, Integer> getPayMap() {
        return payMap;
    }

    public void setPayMap(Map<Integer, Integer> payMap) {
        this.payMap = payMap;
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
