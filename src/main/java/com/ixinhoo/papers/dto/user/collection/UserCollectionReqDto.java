package com.ixinhoo.papers.dto.user.collection;

/**
 * 我的收藏--请求DTO
 *
 * @author cici
 */
public class UserCollectionReqDto implements java.io.Serializable{
    private Long userId;//用户id
    private Integer type;//（3 = '课件', 8 = '教案', 7 = '试卷', 4 = '学案',12 = '视频'）20-试题;0--其他
    private Integer p;//当前页
    private Integer s;//页大小

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getP() {
        return p;
    }

    public void setP(Integer p) {
        this.p = p;
    }

    public Integer getS() {
        return s;
    }

    public void setS(Integer s) {
        this.s = s;
    }
}