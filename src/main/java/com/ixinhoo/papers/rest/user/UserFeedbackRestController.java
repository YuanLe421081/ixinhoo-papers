package com.ixinhoo.papers.rest.user;


import com.chunecai.crumbs.api.client.StatusDto;
import com.ixinhoo.papers.entity.common.UserFeedback;
import com.ixinhoo.papers.service.common.UserFeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户意见反馈
 */
@RestController
@RequestMapping(value = "/api/v1/user/feedback")
public class UserFeedbackRestController {

    @Autowired
    private UserFeedbackService service;


    @RequestMapping(value = "save", method = RequestMethod.POST)
    public StatusDto save(UserFeedback entity) {
        entity.setTime(System.currentTimeMillis());
        service.create(entity);
        return new StatusDto(true);
    }


}