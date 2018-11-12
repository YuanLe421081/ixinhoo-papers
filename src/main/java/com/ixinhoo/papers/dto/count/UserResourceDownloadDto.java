package com.ixinhoo.papers.dto.count;

import com.google.common.collect.Maps;
import com.ixinhoo.papers.entity.base.Area;
import com.ixinhoo.papers.entity.base.Subjects;

import java.util.List;
import java.util.Map;

/**
 * 会员资源下载DTO
 *
 * @author 448778074@qq.com (cici)
 */
public class UserResourceDownloadDto implements java.io.Serializable {
    private UserTendencyChartLineDto tendency;
    private Map<Integer, Integer> typeMap = Maps.newHashMap();//类型分布
    private Map<Integer, Integer> difficultMap = Maps.newHashMap();//难易程度分布
    private Map<Integer, Integer> teachMap = Maps.newHashMap();//教龄
    private Map<Integer, Integer> stageMap = Maps.newHashMap();//学段
    private Map<Integer, Map<Integer, Integer>> gradeMap = Maps.newHashMap();//年级-暂无
    private Map<Integer, Map<Long, Integer>> subjectMap = Maps.newHashMap();//Long学科id
    private Map<Long, Integer> areaMap = Maps.newHashMap();//Long 省份id
    private List<Area> areas;//所有省份
    private List<Subjects> subjectss;//所有学科
    private int count;//总数

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Map<Integer, Integer> getDifficultMap() {
        return difficultMap;
    }

    public void setDifficultMap(Map<Integer, Integer> difficultMap) {
        this.difficultMap = difficultMap;
    }

    public Map<Integer, Map<Long, Integer>> getSubjectMap() {
        return subjectMap;
    }

    public void setSubjectMap(Map<Integer, Map<Long, Integer>> subjectMap) {
        this.subjectMap = subjectMap;
    }

    public UserTendencyChartLineDto getTendency() {
        return tendency;
    }

    public void setTendency(UserTendencyChartLineDto tendency) {
        this.tendency = tendency;
    }

    public Map<Integer, Integer> getTypeMap() {
        return typeMap;
    }

    public void setTypeMap(Map<Integer, Integer> typeMap) {
        this.typeMap = typeMap;
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
