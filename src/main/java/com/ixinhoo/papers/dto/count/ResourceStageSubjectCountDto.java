package com.ixinhoo.papers.dto.count;

/**
 * 资源学段学科查询数据分组返回--DTO
 *
 * @author 448778074@qq.com (cici)
 */
public class ResourceStageSubjectCountDto implements java.io.Serializable {
    private Integer stage;//学段
    private Long subjectId;//学科
    private String subjectName;//学科名称
    private Integer count;//总数

    public Integer getStage() {
        return stage;
    }

    public void setStage(Integer stage) {
        this.stage = stage;
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
