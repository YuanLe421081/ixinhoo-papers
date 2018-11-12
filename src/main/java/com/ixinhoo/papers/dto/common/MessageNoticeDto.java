package com.ixinhoo.papers.dto.common;


/**
 * 消息通知DTO
 *
 * @author cici
 */
public class MessageNoticeDto implements java.io.Serializable{
    private Long id;
    private String title;//标题
    private String content;//内容
    private Integer type;//消息类型，1-系统消息、2系统公告、3平台活动
    private Integer pushType;//推送类型-1所有，2个人、3角色
    private String dataCode;//数据标识，个人为手机号、角色为角色标识
    private Long pushTime;//推送时间
    private Boolean read;//已读？true已读、false未读

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getPushType() {
        return pushType;
    }

    public void setPushType(Integer pushType) {
        this.pushType = pushType;
    }

    public String getDataCode() {
        return dataCode;
    }

    public void setDataCode(String dataCode) {
        this.dataCode = dataCode;
    }

    public Long getPushTime() {
        return pushTime;
    }

    public void setPushTime(Long pushTime) {
        this.pushTime = pushTime;
    }

    public Boolean getRead() {
        return read;
    }

    public void setRead(Boolean read) {
        this.read = read;
    }
}