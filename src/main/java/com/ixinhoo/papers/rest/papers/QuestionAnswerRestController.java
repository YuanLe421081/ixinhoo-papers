package com.ixinhoo.papers.rest.papers;


import com.chunecai.crumbs.api.client.DetailDto;
import com.ixinhoo.papers.dto.papers.QuestionAnswerDto;
import com.ixinhoo.papers.service.papers.QuestionAnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/question-answer")
public class QuestionAnswerRestController {

    @Autowired
    private QuestionAnswerService service;


    /**
     * 题目答案
     *
     * @return
     */
    @RequestMapping(value = "detail", method = RequestMethod.POST)
    public DetailDto<QuestionAnswerDto> detail(Long id) {
        return service.findDetailByQuestionId(id);
    }


}