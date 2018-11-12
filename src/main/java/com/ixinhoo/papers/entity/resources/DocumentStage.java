package com.ixinhoo.papers.entity.resources;

import javax.persistence.Column;

/**
 * 资源文档分表-根据学段进行分表操作
 *
 * @author cici
 */
public class DocumentStage extends BaseDocument {
    @Column
    private Long documentId;//资源id
    @Column
    private Long fileSize;//资源大小
    @Column
    private String uploadAddress;//上传路径
    @Column
    private String downloadAddress;//下载路径
    @Column
    private Long auditTime;//审核时间
    @Column
    private String copyright;//版权信息


    public Long getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getUploadAddress() {
        return uploadAddress;
    }

    public void setUploadAddress(String uploadAddress) {
        this.uploadAddress = uploadAddress;
    }

    public String getDownloadAddress() {
        return downloadAddress;
    }

    public void setDownloadAddress(String downloadAddress) {
        this.downloadAddress = downloadAddress;
    }

    public Long getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(Long auditTime) {
        this.auditTime = auditTime;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }
}