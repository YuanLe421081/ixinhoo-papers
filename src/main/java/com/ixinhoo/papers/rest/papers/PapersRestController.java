package com.ixinhoo.papers.rest.papers;


import com.chunecai.crumbs.api.client.DetailDto;
import com.chunecai.crumbs.api.client.ListDto;
import com.chunecai.crumbs.api.client.PageDto;
import com.ixinhoo.papers.dto.common.CommonIdAndNameDto;
import com.ixinhoo.papers.dto.papers.*;
import com.ixinhoo.papers.dto.user.paper.UserPaperTemplateSaveDto;
import com.ixinhoo.papers.service.papers.PapersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/papers")
public class PapersRestController {

    @Autowired
    private PapersService service;

    /**
     * 组卷--首页底部推荐
     *
     * @return
     */
    @RequestMapping(value = "recommend", method = RequestMethod.GET)
    public ListDto<PapersRecommendDto> recommend() {
        return service.listPapersRecommend();
    }


    /**
     * 组卷--智能组卷--获取组卷类型
     *
     * @return
     */
    @RequestMapping(value = "paper-type", method = RequestMethod.POST)
    public ListDto<CommonIdAndNameDto> paperType(PaperTemplateReqDto reqDto) {
        return service.findPaperTypeInTemplate(reqDto);
    }


    /**
     * 组卷--智能组卷--获取试卷模板
     *
     * @return
     */
    @RequestMapping(value = "paper-template", method = RequestMethod.POST)
    public PageDto<PaperTemplateRspDto> paperTemplate(PaperTemplateReqDto reqDto) {
        return service.findPaperByTemplate(reqDto);
    }

    /**
     * 组卷--智能组卷--获取试卷分析数据
     *
     * @return
     */
    @RequestMapping(value = "paper-analysis", method = RequestMethod.POST)
    public DetailDto<PaperAnalysisDto> paperAnalysis(Long id) {
        return service.paperAnalysis(id);
    }

    /**
     * 试卷库--同步测试类型搜索
     *
     * @return
     */
    @RequestMapping(value = "search-chapter", method = RequestMethod.POST)
    public PageDto<PapersSearchRspDto> searchByChapter(PapersSearchByChapterReqDto reqDto) {
        return service.searchByChapter(reqDto);
    }

    /**
     * 试卷库--升学考试类型搜索
     *
     * @return
     */
    @RequestMapping(value = "search-grade", method = RequestMethod.POST)
    public PageDto<PapersSearchRspDto> searchByGrade(PapersSearchByGradeReqDto reqDto) {
        return service.searchByGrade(reqDto);
    }

    /**
     * 智能组卷--查看试卷模板模板信息； TODO cici 废弃
     *
     * @return
     */
    @RequestMapping(value = "template-detail", method = RequestMethod.POST)
    public DetailDto<UserPaperTemplateSaveDto> templateDetail(Long id) {
        return service.findPaperTemplateById(id);
    }


}