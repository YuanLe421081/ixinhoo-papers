package com.ixinhoo.papers.rest.user;


import com.chunecai.crumbs.api.client.ListDto;
import com.chunecai.crumbs.api.client.PageDto;
import com.chunecai.crumbs.api.client.StatusDto;
import com.ixinhoo.papers.dto.base.StageSubjectDto;
import com.ixinhoo.papers.dto.user.collection.UserCollectionQuestionReqDto;
import com.ixinhoo.papers.dto.user.collection.UserCollectionQuestionRspDto;
import com.ixinhoo.papers.dto.user.collection.UserCollectionReqDto;
import com.ixinhoo.papers.dto.user.collection.UserCollectionRspDto;
import com.ixinhoo.papers.service.user.UserCollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 用户收藏
 */
@RestController
@RequestMapping(value = "/api/v1/user-collection")
public class UserCollectionRestController {

    @Autowired
    private UserCollectionService service;

    /**
     * 加入收藏--批量加入；--从购物车中批量收藏
     *
     * @return
     */
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public StatusDto add(Long userId,@RequestParam List<Long> documentIds) {
        return service.createByUserAndDocument(userId, documentIds);
    }


    /**
     * 加入收藏--试题类型
     *
     * @return
     */
    @RequestMapping(value = "add-question", method = RequestMethod.POST)
    public StatusDto addQuestion(Long userId,@RequestParam List<Long> questionIds) {
        return service.createByUserAndQuestion(userId, questionIds);
    }

    /**
     * 取消收藏--试题类型
     *
     * @return
     */
    @RequestMapping(value = "cancel-question", method = RequestMethod.POST)
    public StatusDto cancelQuestion(Long userId,@RequestParam List<Long> questionIds) {
        return service.deleteByUserAndQuestion(userId, questionIds);
    }


    /**
     * 收藏或者取消--试题类型；如果收藏再次调用则取消收藏;
     * code--1收藏成功、2取消收藏成功
     *
     * @return
     */
    @RequestMapping(value = "switch-question", method = RequestMethod.POST)
    public StatusDto switchQuestion(Long userId,Long questionId) {
        return service.updateByUserAndQuestion(userId, questionId);
    }

    /**
     * 收藏列表;--除试题外；
     * （3 = '课件', 8 = '教案', 7 = '试卷', 4 = '学案',12 = '视频'）20-试题;0--其他
     *
     * @return
     */
    @RequestMapping(value = "list", method = RequestMethod.POST)
    public PageDto<UserCollectionRspDto> list(UserCollectionReqDto reqDto) {
        return service.listDocumentByUserCollection(reqDto);
    }


    /**
     * 收藏列表;--试题--用户收藏中包含的学段和学科
     *
     * @return
     */
    @RequestMapping(value = "stage-subject", method = RequestMethod.POST)
    public ListDto<StageSubjectDto> listStageAndSubject(Long userId) {
        return service.listStageAndSubject(userId);
    }

    /**
     * 收藏列表;--试题；
     *
     * @return
     */
    @RequestMapping(value = "list-question", method = RequestMethod.POST)
    public PageDto<UserCollectionQuestionRspDto> listQuestion(UserCollectionQuestionReqDto reqDto) {
        return service.listQuestionByUserCollection(reqDto);
    }

    /**
     * 批量取消收藏
     *
     * @return
     */
        @RequestMapping(value = "delete", method = RequestMethod.POST)
    public StatusDto delete(@RequestParam List<Long> ids) {
        service.deleteByIds(ids);
        return new StatusDto(true);
    }


}