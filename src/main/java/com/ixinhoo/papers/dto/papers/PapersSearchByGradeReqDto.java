package com.ixinhoo.papers.dto.papers;

/**
 * 试卷库搜索-升学考试-请求DTO
 *
 * @author cici
 */
public class PapersSearchByGradeReqDto implements java.io.Serializable{
    private Integer p;//当前页
    private Integer s;//页大小
    private Integer grade;//年级
    private String bookName;//册别名称
    private Long provinceId;//地区id
    private String name;//搜索名称
    private String sortName;//排序名称，updatedAt--更新时间，downloadNum--下载量
    private String desc;//排序,降序desc/升序asc

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

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public Long getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Long provinceId) {
        this.provinceId = provinceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSortName() {
        return sortName;
    }

    public void setSortName(String sortName) {
        this.sortName = sortName;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}