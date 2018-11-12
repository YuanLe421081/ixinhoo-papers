package com.ixinhoo.papers.dto.base;

import com.ixinhoo.papers.dto.common.CommonIdAndNameDto;

import java.util.List;

/**
 * 学段下面的学科信息
 *
 * @author 448778074@qq.com (cici)
 */
public class StageSubjectDto implements java.io.Serializable{
    private Integer stage;//学段
    private List<CommonIdAndNameDto> subjects;//学科

    public Integer getStage() {
        return stage;
    }

    public void setStage(Integer stage) {
        this.stage = stage;
    }

    public List<CommonIdAndNameDto> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<CommonIdAndNameDto> subjects) {
        this.subjects = subjects;
    }
}
