package com.ixinhoo.papers.dto.resources;

/**
 * 备课搜索-响应DTO
 *
 * @author cici
 */
public class DocumentSearchRspDto implements java.io.Serializable{
    private Long id;//主键
    private Integer stage;//学段
    private Long subjectId;//学科id
    private Long versionId;//版本id
    private Long userId;//上传用户的主键id
    private String userName;//上传用户姓名
    private String title;//标题
    private Long createdAt;//创建时间
    private Long updatedAt;//更新时间
    private Integer coin;//资料所需备课币
    private Integer year;//年份
    private Integer typeId;//类型
    private String intro;//资料简介
    private String fileType;//文件类型(ppt, doc, rar, video)
    private String coverImage;//封面
    private Long downloadNum;//下载次数
    private Double score;//评分总分
    private Long scoreNum;//评分人数

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getStage() {
        return stage;
    }

    public void setStage(Integer stage) {
        this.stage = stage;
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    public Long getVersionId() {
        return versionId;
    }

    public void setVersionId(Long versionId) {
        this.versionId = versionId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getCoin() {
        return coin;
    }

    public void setCoin(Integer coin) {
        this.coin = coin;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
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