package com.ixinhoo.papers.entity.resources;

import com.chunecai.crumbs.api.entity.AuditEntity;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * 文档扩展表，id与文档一致
 *
 * @author cici
 */
@Table(name = "document_extend")
public class DocumentExtend01 extends AuditEntity {

    @Column
    private Long viewNum;//预览次数
    @Column
    private Long downloadNum;//下载次数
    @Column
    private Double score;//评分总分
    @Column
    private Long scoreNum;//评分人数
    @Column
    private Double stars;//星级

    public Double getStars() {
        return stars;
    }

    public void setStars(Double stars) {
        this.stars = stars;
    }

    public Long getViewNum() {
        return viewNum;
    }

    public void setViewNum(Long viewNum) {
        this.viewNum = viewNum;
    }

    public Long getDownloadNum() {
        return downloadNum;
    }

    public void setDownloadNum(Long downloadNum) {
        this.downloadNum = downloadNum;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Long getScoreNum() {
        return scoreNum;
    }

    public void setScoreNum(Long scoreNum) {
        this.scoreNum = scoreNum;
    }
}