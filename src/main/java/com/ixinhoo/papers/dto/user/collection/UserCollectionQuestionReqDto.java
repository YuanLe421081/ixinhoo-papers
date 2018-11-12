package com.ixinhoo.papers.dto.user.collection;

/**
 * 我的收藏--试题类型请求DTO
 *
 * @author cici
 */
public class UserCollectionQuestionReqDto implements java.io.Serializable{
    private Long userId;
    private Integer stage;//学段
    private Long subjectId;//学科
    private Integer p;
    private Integer s;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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
}