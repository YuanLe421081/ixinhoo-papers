package com.ixinhoo.papers.rest.papers;


import com.chunecai.crumbs.api.client.ListDto;
import com.ixinhoo.papers.dto.common.CommonIdAndNameDto;
import com.ixinhoo.papers.service.papers.QuestionTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/question-type")
public class QuestionTypeRestController {

    @Autowired
    private QuestionTypeService service;



    /**
     * 查询所有题类
     * @return
     */
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public ListDto<CommonIdAndNameDto> list() {
        return service.findAll();
    }


}