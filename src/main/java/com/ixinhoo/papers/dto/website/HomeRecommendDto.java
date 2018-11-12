package com.ixinhoo.papers.dto.website;

/**
 * 首页精品推荐DTO
 *
 * @author cici
 */
public class HomeRecommendDto implements java.io.Serializable{
    private Long subjectId;//学科id
    private String subjectName;//学科名称
    private Long versionId;//版本id
    private String versionName;//版本名称
    private Integer typeId;//资源类型[3 = '课件', 8 = '教案', 7 = '试卷', 4 = '学案',11= '资源包',6 = '素材',12 = '视频']
    private String typeName;//资源类型名称
    private String title;//资源标题
    private Long id;//资源id
    private Integer stage;//资源学段
    private String coverImage;//资源封面

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public Long getVersionId() {
        return versionId;
    }

    public void setVersionId(Long versionId) {
        this.versionId = versionId;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getStage() {
        return stage;
    }

    public void setStage(Integer stage) {
        this.stage = stage;
    }
}