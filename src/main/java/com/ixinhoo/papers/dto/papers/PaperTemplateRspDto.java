package com.ixinhoo.papers.dto.papers;

/**
 * 试卷模板响应DTO;
 *
 * @author 448778074@qq.com (cici)
 */
public class PaperTemplateRspDto implements java.io.Serializable{
    private Long id;//主键
    private Long typeId;//试卷类型id
    private String typeName;//试卷类型名称
    private String title;//标题
    private Long usedNum;//使用次数

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

    public Long getUsedNum() {
        return usedNum;
    }

    public void setUsedNum(Long usedNum) {
        this.usedNum = usedNum;
    }
}
