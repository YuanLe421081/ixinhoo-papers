package com.ixinhoo.papers.entity.papers;

import com.chunecai.crumbs.api.entity.AuditEntity;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * 试卷扩展表，id与试卷一致
 *
 * @author cici
 */
@Table(name = "papers_extend")
public class PapersExtend extends AuditEntity {
    @Column
    private Long viewNum;//预览次数
    @Column
    private Long usedNum;//使用次数

    public Long getViewNum() {
        return viewNum;
    }

    public void setViewNum(Long viewNum) {
        this.viewNum = viewNum;
    }

    public Long getUsedNum() {
        return usedNum;
    }

    public void setUsedNum(Long usedNum) {
        this.usedNum = usedNum;
    }
}