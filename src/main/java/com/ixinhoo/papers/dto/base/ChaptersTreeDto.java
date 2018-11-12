package com.ixinhoo.papers.dto.base;

import java.util.List;

/**
 * 首页-tree的DTO
 *
 * @author cici
 */
public class ChaptersTreeDto implements java.io.Serializable{
    private Long id;//学科id
    private String name;//学科名称
    private List<ChaptersStageDto> stages;//学段

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

    public List<ChaptersStageDto> getStages() {
        return stages;
    }

    public void setStages(List<ChaptersStageDto> stages) {
        this.stages = stages;
    }
}