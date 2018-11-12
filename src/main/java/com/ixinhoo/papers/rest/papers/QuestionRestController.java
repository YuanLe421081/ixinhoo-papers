package com.ixinhoo.papers.rest.papers;


import com.chunecai.crumbs.api.client.DetailDto;
import com.chunecai.crumbs.api.client.ListDto;
import com.chunecai.crumbs.api.client.PageDto;
import com.ixinhoo.papers.dto.common.CommonIdAndNameDto;
import com.ixinhoo.papers.dto.papers.*;
import com.ixinhoo.papers.service.papers.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/question")
public class QuestionRestController {

    @Autowired
    private QuestionService service;

    /**
     * 搜索
     *
     * @return
     */
    @RequestMapping(value = "search", method = RequestMethod.POST)
    public PageDto<QuestionSearchRspDto> search(QuestionSearchReqDto reqDto) {
        return service.searchQuestion(reqDto);
    }


    /**
     * 生成试卷--选题生成
     *
     * @return
     */
    @RequestMapping(value = "make-paper", method = RequestMethod.POST)
    public DetailDto<PapersQuestionDto> makePaper(@RequestParam List<Long> questionIds) {
        return service.makePapersQuestion(questionIds);
    }


    /**
     * 生成试卷--（新建试卷）-智能组卷生成--章节
     *
     * @return
     */
    @RequestMapping(value = "template-chapter", method = RequestMethod.POST)
    public DetailDto<PapersQuestionDto> templateChapter(@RequestBody ChapterToPaperDto reqDto) {
        return service.makeTemplatePapersQuestionByChapter(reqDto);
    }

    /**
     * 生成试卷--（新建试卷）-智能组卷生成--知识点
     *
     * @return
     */
    @RequestMapping(value = "template-knowledge", method = RequestMethod.POST)
    public DetailDto<PapersQuestionDto> templateKnowledge(@RequestBody ChapterToPaperDto reqDto) {
        return service.makeTemplatePapersQuestionByKnowledge(reqDto);
    }

    /**
     * 根据试卷的id查询试卷下的试题信息;
     *
     * @return
     */
    @RequestMapping(value = "paper", method = RequestMethod.POST)
    public DetailDto<PapersQuestionDto> paper(Long paperId) {
        return service.makePapersByPaperId(paperId);
    }

    /**
     * 换题 ; --根据试题的主键id查询相类似的题目分页显示;
     *
     * @return
     */
    @RequestMapping(value = "change", method = RequestMethod.POST)
    public ListDto<QuestionChangeDto> change(Long questionId, Integer p, Integer s) {
        return service.changeQuestionById(questionId, p, s);
    }


    /**
     * 根据章节的id集合，查询章节下面的题型题量及章节范围 ；--组卷
     *
     * @return
     */
    @RequestMapping(value = "type-chapter", method = RequestMethod.POST)
    public DetailDto<QuestionTypeAndChapterDto> typeAndChapter(@RequestParam List<Long> ids) {
        return service.findTypeAndCountQuestion(ids);
    }

    /**
     * 获取知识点列表--组卷--新建用户模板--根据选择的学科信息和题型id查询
     *
     * @return
     */
    @RequestMapping(value = "type-knowledge", method = RequestMethod.POST)
    public ListDto<CommonIdAndNameDto> knowledge(PaperTemplateReqDto reqDto) {
        return service.findQuestionKnowledge(reqDto);
    }

}