package com.ixinhoo.papers.dto.user;

/**
 * 用户订单列表数据DTO
 *
 * @author cici
 */
public class UserOrderListDto implements java.io.Serializable{
    private Long id;//主键id
    private Long userId;//用户id
    private Long time;//订单时间
    private String userName;//用户姓名
    private String preNum;//预支付订单号，用于前端手机查询，
    private String orderNum;//平台订单号
    private String thirdNum;//第三方订单号
    private Integer payType;//支付方式，1-余额支付、2-微信支付、3支付宝支付
    private Long dataId;//数据主键id
    private Double price;//订单金额
    private Integer coin;//备课币
    private Integer status;//状态；1-成功、2-失败、3-待确认
    private Integer type;//类型，1-充值、2-下载、3-组卷
    private Integer source;//订单来源，1-PC、2-ANDROID、3-IOS
    private Long payTime;//支付完成时间
    private Object dataDetail;//数据节点

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPreNum() {
        return preNum;
    }

    public void setPreNum(String preNum) {
        this.preNum = preNum;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getThirdNum() {
        return thirdNum;
    }

    public void setThirdNum(String thirdNum) {
        this.thirdNum = thirdNum;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public Long getDataId() {
        return dataId;
    }

    public void setDataId(Long dataId) {
        this.dataId = dataId;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public Long getPayTime() {
        return payTime;
    }

    public void setPayTime(Long payTime) {
        this.payTime = payTime;
    }

    public Object getDataDetail() {
        return dataDetail;
    }

    public void setDataDetail(Object dataDetail) {
        this.dataDetail = dataDetail;
    }
}