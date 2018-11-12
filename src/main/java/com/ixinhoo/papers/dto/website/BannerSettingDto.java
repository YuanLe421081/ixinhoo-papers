package com.ixinhoo.papers.dto.website;

/**
 * 广告banner设置
 *
 * @author cici
 */
public class BannerSettingDto implements java.io.Serializable{
    private Long id;
    private String image;//图片地址
    private String name;//名称
    private Integer sort;//排序
    private Integer type;//banner类型。1首页轮播、2组卷轮播
    private Long dataId;//数据id
    private String link;//链接地址

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getDataId() {
        return dataId;
    }

    public void setDataId(Long dataId) {
        this.dataId = dataId;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}