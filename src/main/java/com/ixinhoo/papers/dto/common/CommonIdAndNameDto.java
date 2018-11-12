package com.ixinhoo.papers.dto.common;

/**
 * 学科信息&学段信息等；id和名称通用dto
 *
 * @author cici
 */
public class CommonIdAndNameDto implements java.io.Serializable{
    private Long id;//主键
    private String name;//名称

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
}