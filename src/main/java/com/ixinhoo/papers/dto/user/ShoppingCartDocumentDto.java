package com.ixinhoo.papers.dto.user;

/**
 * 用户购物车-DTO
 *
 * @author cici
 */
public class ShoppingCartDocumentDto implements java.io.Serializable{
    private Long id;//文档主键
    private Long cartId;//购物车主键
    private String title;//标题
    private Integer coin;//资料所需备课币
    private String fileType;//文件类型(ppt, doc, rar, video)
    private String coverImage;//封面
    private Integer stage;//学段

    public Integer getStage() {
        return stage;
    }

    public void setStage(Integer stage) {
        this.stage = stage;
    }

    public Long getCartId() {
        return cartId;
    }

    public void setCartId(Long cartId) {
        this.cartId = cartId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
}