package com.ixinhoo.papers.rest.base;


import com.chunecai.crumbs.api.client.ListDto;
import com.ixinhoo.papers.dto.common.CommonIdAndNameDto;
import com.ixinhoo.papers.service.base.SubjectsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/subjects")
public class SubjectsRestController {

    @Autowired
    private SubjectsService service;

    /**
     * 查询学段下面的学科信息
     *
     * @param stage
     * @return
     */
    @RequestMapping(value = "stage", method = RequestMethod.POST)
    public ListDto<CommonIdAndNameDto> findByStage(Integer stage) {
        return service.findByStage(stage);
    }


}