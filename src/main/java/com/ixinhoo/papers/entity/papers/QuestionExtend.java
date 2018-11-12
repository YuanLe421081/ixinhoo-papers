package com.ixinhoo.papers.entity.papers;

import com.chunecai.crumbs.api.entity.AuditEntity;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * 试题扩展表，id与试卷一致
 *
 * @author cici
 */
@Table(name = "question_extend")
public class QuestionExtend extends AuditEntity {
    @Column
    private Long usedNum;//使用组卷次数

    public Long getUsedNum() {
        return usedNum;
    }

    public void setUsedNum(Long usedNum) {
        this.usedNum = usedNum;
    }
}