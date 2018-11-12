package com.ixinhoo.papers.entity.resources;

import com.chunecai.crumbs.api.entity.AuditEntity;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * 资源文档-章节
 *
 * @author cici
 */
@Table(name = "document_chapter")
public class DocumentChapter extends AuditEntity {
    @Column
    private Long documentId;//资源id
    @Column
    private Long chapterId;//章节id
    @Column
    private String chapterPath;//章节路径
    @Column
    private String chapterName;//章节名称
    @Column
    private String documentCode;//资源标识

    public Long getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
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

    public String getDocumentCode() {
        return documentCode;
    }

    public void setDocumentCode(String documentCode) {
        this.documentCode = documentCode;
    }
}