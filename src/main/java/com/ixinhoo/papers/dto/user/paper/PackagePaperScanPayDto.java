package com.ixinhoo.papers.dto.user.paper;

/**
 * 组卷--扫码下载 DTO
 *
 * @author 448778074@qq.com (cici)
 */
public class PackagePaperScanPayDto implements java.io.Serializable{
    private Integer userCoin;//用户备课币余额
    private Integer downloadIcon;//下载所需备课币
    private Double price;//当前文件所需金额
    private String wechatQrcode;//微信二维码base64返回
    private String wechatNum;//微信订单号
    private String alipayQrcode;//支付宝二维码，前端插入到页面
    private String alipayNum;//支付宝订单号

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

    public Integer getUserCoin() {
        return userCoin;
    }

    public void setUserCoin(Integer userCoin) {
        this.userCoin = userCoin;
    }

    public Integer getDownloadIcon() {
        return downloadIcon;
    }

    public void setDownloadIcon(Integer downloadIcon) {
        this.downloadIcon = downloadIcon;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getWechatQrcode() {
        return wechatQrcode;
    }

    public void setWechatQrcode(String wechatQrcode) {
        this.wechatQrcode = wechatQrcode;
    }

    public String getAlipayQrcode() {
        return alipayQrcode;
    }

    public void setAlipayQrcode(String alipayQrcode) {
        this.alipayQrcode = alipayQrcode;
    }
}
