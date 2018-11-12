package com.ixinhoo.papers.rest.user;


import com.chunecai.crumbs.api.client.StatusDto;
import com.ixinhoo.papers.service.user.UserScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户评分
 */
@RestController
@RequestMapping(value = "/api/v1/user-score")
public class UserScoreRestController {

    @Autowired
    private UserScoreService service;

    /**
     * 文档资源评分
     *
     * @return
     */
    @RequestMapping(value = "document", method = RequestMethod.POST)
    public StatusDto document(Long userId, Long documentId, Integer score) {
        return service.createByUserAndDocument(userId, documentId, score);
    }


}