package com.ixinhoo.papers.dto.papers;

/**
 * 试卷库搜索-响应DTO
 *
 * @author cici
 */
public class PapersSearchRspDto implements java.io.Serializable{
    private Long id;//试卷id
    private Long updatedAt;//更新时间
    private Long downloadNum;//下载量
    private Integer coin;//试卷备课币
    private String title;//标题

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Long getDownloadNum() {
        return downloadNum;
    }

    public void setDownloadNum(Long downloadNum) {
        this.downloadNum = downloadNum;
    }

    public Integer getCoin() {
        return coin;
    }

    public void setCoin(Integer coin) {
        this.coin = coin;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}