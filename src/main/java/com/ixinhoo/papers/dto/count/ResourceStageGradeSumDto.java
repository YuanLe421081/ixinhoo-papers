package com.ixinhoo.papers.dto.count;

/**
 * 资源学段年级查询数据分组返回--DTO
 *
 * @author 448778074@qq.com (cici)
 */
public class ResourceStageGradeSumDto implements java.io.Serializable {
    private Integer stage;//学段
    private Integer grade;//年级
    private Double count;//总数

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

    public Double getCount() {
        return count;
    }

    public void setCount(Double count) {
        this.count = count;
    }
}
