package com.ixinhoo.papers.rest.user;


import com.chunecai.crumbs.api.client.DetailDto;
import com.chunecai.crumbs.api.client.ListDto;
import com.chunecai.crumbs.api.client.StatusDto;
import com.chunecai.crumbs.code.util.mapper.BeanMapper;
import com.ixinhoo.papers.dto.papers.PapersQuestionDto;
import com.ixinhoo.papers.dto.user.paper.UserPaperTemplateSaveDto;
import com.ixinhoo.papers.entity.user.UserPaperTemplate;
import com.ixinhoo.papers.service.user.UserPaperTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户模板
 */
@RestController
@RequestMapping(value = "/api/v1/user-paper-template")
public class UserPaperTemplateRestController {

    @Autowired
    private UserPaperTemplateService service;

    /**
     * 保存用户模板
     *
     * @return
     */
    @RequestMapping(value = "save", method = RequestMethod.POST)
    public StatusDto save(@RequestBody UserPaperTemplateSaveDto reqDto) {
        return service.saveUserPaperTemplate(reqDto);
    }

    /**
     * 生成试卷--（新建试卷）-智能组卷生成--我的模板--新建模板生成
     *
     * @return
     */
    @RequestMapping(value = "make-paper", method = RequestMethod.POST)
    public DetailDto<PapersQuestionDto> makePaper(@RequestBody UserPaperTemplateSaveDto reqDto) {
        return service.makeTemplatePapersQuestion(reqDto);
    }

    /**
     * 智能组卷--查看用户模板信息
     *
     * @return
     */
    @RequestMapping(value = "detail", method = RequestMethod.POST)
    public DetailDto<UserPaperTemplateSaveDto> detail(Long id) {
        return service.findUserPaperTemplateById(id);
    }

    /**
     * 我的模板列表
     *
     * @return
     */
    @RequestMapping(value = "list", method = RequestMethod.POST)
    public ListDto<UserPaperTemplate> list(Long userId) {
        return service.findByUserId(userId);
    }

    /**
     * 我的模板列表--删除模板记录
     *
     * @return
     */
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public StatusDto delete(@RequestParam List<Long> ids) {
        service.deleteByIds(ids);
        return new StatusDto(true);
    }



}