package com.ixinhoo.papers.dto.user.collection;

/**
 * 我的收藏
 *
 * @author cici
 */
public class UserCollectionRspDto implements java.io.Serializable{
    private Long id;//文档主键
    private Long collectionId;//收藏主键
    private String title;//标题
    private Integer coin;//资料所需备课币
    private String fileType;//文件类型(ppt, doc, rar, video)
    private String coverImage;//封面
    private Long time;//收藏时间
    private Double score;//评分总分
    private Long scoreNum;//评分人数

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(Long collectionId) {
        this.collectionId = collectionId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getCoin() {
        return coin;
    }

    public void setCoin(Integer coin) {
        this.coin = coin;
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