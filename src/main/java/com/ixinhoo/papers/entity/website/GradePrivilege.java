package com.ixinhoo.papers.entity.website;

import com.chunecai.crumbs.api.entity.AuditEntity;

import javax.persistence.Table;

/**
 * 等级特权--初始化-内置数据
 *
 * @author cici
 */
@Table(name = "grade_privilege")
public class GradePrivilege extends AuditEntity {
    private Long gradeId;//等级id
    private String gradeCode;//等级标识
    private Integer monthFreeNum;//每月免费资料份数
    private Double downloadDiscount;//下载折扣
    private Double paperDiscount;//组卷折扣

    public Long getGradeId() {
        return gradeId;
    }

    public void setGradeId(Long gradeId) {
        this.gradeId = gradeId;
    }

    public String getGradeCode() {
        return gradeCode;
    }

    public void setGradeCode(String gradeCode) {
        this.gradeCode = gradeCode;
    }

    public Integer getMonthFreeNum() {
        return monthFreeNum;
    }

    public void setMonthFreeNum(Integer monthFreeNum) {
        this.monthFreeNum = monthFreeNum;
    }

    public Double getDownloadDiscount() {
        return downloadDiscount;
    }

    public void setDownloadDiscount(Double downloadDiscount) {
        this.downloadDiscount = downloadDiscount;
    }

    public Double getPaperDiscount() {
        return paperDiscount;
    }

    public void setPaperDiscount(Double paperDiscount) {
        this.paperDiscount = paperDiscount;
    }
}