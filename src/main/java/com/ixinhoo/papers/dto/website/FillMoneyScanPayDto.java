package com.ixinhoo.papers.dto.website;

/**
 * 在线支付--扫码支付DTO
 *
 * @author cici
 */
public class FillMoneyScanPayDto implements java.io.Serializable{
    private Long id;//数据主键
    private Double price;//金额
    private Integer coin;//所得的备课币
    private String wechatNum;//微信预支付订单（用于查询）
    private String wechatUrl;//微信二维码信息
    private String wechatQrcode;//微信二维码转base64
    private String alipayNum;//支付宝预支付订单（用于查询）
    private String alipayUrl;//支付宝二维码信息（暂无）
    private String alipayQrcode;//支付宝二维码

    public String getWechatNum() {
        return wechatNum;
    }

    public void setWechatNum(String wechatNum) {
        this.wechatNum = wechatNum;
    }

    public String getAlipayNum() {
        return alipayNum;
    }

    public void setAlipayNum(String alipayNum) {
        this.alipayNum = alipayNum;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getCoin() {
        return coin;
    }

    public void setCoin(Integer coin) {
        this.coin = coin;
    }

    public String getWechatUrl() {
        return wechatUrl;
    }

    public void setWechatUrl(String wechatUrl) {
        this.wechatUrl = wechatUrl;
    }

    public String getWechatQrcode() {
        return wechatQrcode;
    }

    public void setWechatQrcode(String wechatQrcode) {
        this.wechatQrcode = wechatQrcode;
    }

    public String getAlipayUrl() {
        return alipayUrl;
    }

    public void setAlipayUrl(String alipayUrl) {
        this.alipayUrl = alipayUrl;
    }

    public String getAlipayQrcode() {
        return alipayQrcode;
    }

    public void setAlipayQrcode(String alipayQrcode) {
        this.alipayQrcode = alipayQrcode;
    }
}