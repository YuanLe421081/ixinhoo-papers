package com.ixinhoo.papers.dto.resources;

/**
 * 资源详情-预览信息-DTO
 *
 * @author cici
 */
public class DocumentViewDto implements java.io.Serializable{
    private String title;//资源标题
    private String fileType;//文件类型
    private Long fileSize;//文件大小
    private String fileUrl;//预览路径

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