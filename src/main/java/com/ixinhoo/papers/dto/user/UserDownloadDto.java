package com.ixinhoo.papers.dto.user;

/**
 * 我的下载
 *
 * @author cici
 */
public class UserDownloadDto implements java.io.Serializable {
    private Long id;//文档主键
    private Long downloadId;//下载主键
    private String title;//标题
    private String fileType;//文件类型(ppt, doc, rar, video)
    private String coverImage;//封面
    private Long time;//下载时间
    private Double score;//评分总分
    private Long scoreNum;//评分人数

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDownloadId() {
        return downloadId;
    }

    public void setDownloadId(Long downloadId) {
        this.downloadId = downloadId;
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

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
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