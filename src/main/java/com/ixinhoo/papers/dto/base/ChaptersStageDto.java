package com.ixinhoo.papers.dto.base;

import java.util.Set;

/**
 * 首页tree-学段相关数据
 *
 * @author cici
 */
public class ChaptersStageDto implements java.io.Serializable{
    private Integer stage;
    private Set<Integer> grades;//年级集合

    public Integer getStage() {
        return stage;
    }

    public void setStage(Integer stage) {
        this.stage = stage;
    }

    public Set<Integer> getGrades() {
        return grades;
    }

    public void setGrades(Set<Integer> grades) {
        this.grades = grades;
    }


}