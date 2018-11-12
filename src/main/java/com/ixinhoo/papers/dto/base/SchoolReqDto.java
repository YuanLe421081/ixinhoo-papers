package com.ixinhoo.papers.dto.base;

/**
 * 学校查询DTO
 *
 * @author 448778074@qq.com (cici)
 */
public class SchoolReqDto implements java.io.Serializable{
    private Long provinceId;//省份id
    private String schoolName;//学校名称
    private Long stage;//学段0--全部、3--高中、2--初中、1--小学

    public Long getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Long provinceId) {
        this.provinceId = provinceId;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public Long getStage() {
        return stage;
    }

    public void setStage(Long stage) {
        this.stage = stage;
    }
}
