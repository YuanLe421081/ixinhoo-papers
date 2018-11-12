package com.ixinhoo.papers.dto.count;

/**
 *  资源类型查询数据分组返回--DTO
 *
 * @author 448778074@qq.com (cici)
 */
public class ResourceTypeCountDto implements java.io.Serializable {
    private Long id;//id
    private String name;//名称
    private Integer count;//总数

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
