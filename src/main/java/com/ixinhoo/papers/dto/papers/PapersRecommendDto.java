package com.ixinhoo.papers.dto.papers;

/**
 * 试卷--推荐DTO
 *
 * @author cici
 */
public class PapersRecommendDto implements java.io.Serializable{
    private Long id;//主键
    private Long typeId;//试卷类型id
    private String typeName;//试卷类型名称
    private String title;//标题
    private Long createdAt;//创建时间
    private Long updatedAt;//更新时间

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }
}