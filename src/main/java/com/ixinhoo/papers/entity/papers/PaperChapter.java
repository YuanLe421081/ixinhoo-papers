package com.ixinhoo.papers.entity.papers;

import com.chunecai.crumbs.api.entity.AuditEntity;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * 试卷-章节
 *
 * @author cici
 */
@Table(name = "paper_chapter")
public class PaperChapter extends AuditEntity {
    @Column
    private Long paperId;//试卷id
    @Column
    private Long chapterId;//章节id
    @Column
    private String chapterPath;//章节路径
    @Column
    private String chapterName;//章节名称
    @Column
    private String paperCode;//试卷标识

    public String getPaperCode() {
        return paperCode;
    }

    public void setPaperCode(String paperCode) {
        this.paperCode = paperCode;
    }

    public Long getPaperId() {
        return paperId;
    }

    public void setPaperId(Long paperId) {
        this.paperId = paperId;
    }

    public Long getChapterId() {
        return chapterId;
    }

    public void setChapterId(Long chapterId) {
        this.chapterId = chapterId;
    }

    public String getChapterPath() {
        return chapterPath;
    }

    public void setChapterPath(String chapterPath) {
        this.chapterPath = chapterPath;
    }

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }
}