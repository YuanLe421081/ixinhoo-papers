package com.ixinhoo.papers.entity.resources;

import com.chunecai.crumbs.api.entity.AuditEntity;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * 资源文档-预览表
 *
 * @author cici
 */
@Table(name = "document_view")
public class DocumentView extends AuditEntity {
    @Column
    private Long documentId;//资源id
    @Column
    private String documentCode;//资源标识
    @Column
    private String title;//资源标题
    @Column
    private String fileType;//文件类型
    @Column
    private Long fileSize;//文件大小
    @Column
    private String fileUrl;//预览路径

    public Long getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }

    public String getDocumentCode() {
        return documentCode;
    }

    public void setDocumentCode(String documentCode) {
        this.documentCode = documentCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }
}