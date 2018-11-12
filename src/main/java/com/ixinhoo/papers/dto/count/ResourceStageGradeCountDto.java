package com.ixinhoo.papers.dto.count;

/**
 * 资源学段年级查询数据分组返回--DTO
 *
 * @author 448778074@qq.com (cici)
 */
public class ResourceStageGradeCountDto implements java.io.Serializable {
    private Integer stage;//学段
    private Integer grade;//年级
    private Integer count;//总数

    public Integer getStage() {
        return stage;
    }

    public void setStage(Integer stage) {
        this.stage = stage;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
