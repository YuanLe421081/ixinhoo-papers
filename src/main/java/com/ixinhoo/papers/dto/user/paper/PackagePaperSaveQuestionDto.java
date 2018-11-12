package com.ixinhoo.papers.dto.user.paper;

/**
 * 保存用户组卷信息-问题
 *
 * @author 448778074@qq.com (cici)
 */
public class PackagePaperSaveQuestionDto implements java.io.Serializable{
    private Long id;//问题id
    private Integer score;//分数

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }
}
