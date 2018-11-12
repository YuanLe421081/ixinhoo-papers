package com.ixinhoo.papers.dao.papers;

import com.chunecai.crumbs.api.dao.BaseDao;
import com.ixinhoo.config.redis.RedisCache;
import com.ixinhoo.papers.dto.count.ResourceStageGradeCountDto;
import com.ixinhoo.papers.dto.count.ResourceStageSubjectCountDto;
import com.ixinhoo.papers.dto.count.ResourceTypeCountDto;
import com.ixinhoo.papers.dto.papers.*;
import com.ixinhoo.papers.entity.papers.Papers;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

//@CacheNamespace(implementation = RedisCache.class)
public interface PapersDao extends BaseDao<Papers> {
    /**
     * 组卷首页推荐--精品推荐
     *
     * @return
     */
    @SelectProvider(
            type = PapersDaoProvider.class,
            method = "selectPapersRecommendByPage"
    )
    List<Papers> selectPapersRecommendByPage(@Param("p") Integer p, @Param("s") Integer s);

    /**
     * 组卷--智能组卷--获取组卷类型
     *
     * @return
     */
    @SelectProvider(
            type = PapersDaoProvider.class,
            method = "selectDistinctByTemplate"
    )
    List<Papers> selectDistinctByTemplate(PaperTemplateReqDto reqDto);

    /**
     * 组卷--智能组卷--获取试卷模板--分页
     *
     * @return
     */
    @SelectProvider(
            type = PapersDaoProvider.class,
            method = "selectPaperByTemplate"
    )
    List<PaperTemplateRspDto> selectPaperByTemplate(PaperTemplateReqDto reqDto);

    /**
     * 组卷--智能组卷--获取试卷模板--总数
     *
     * @return
     */
    @SelectProvider(
            type = PapersDaoProvider.class,
            method = "selectCountPaperByTemplate"
    )
    int selectCountPaperByTemplate(PaperTemplateReqDto reqDto);

    /**
     * 试卷库---同步测试--搜索--分页
     *
     * @param reqDto
     * @return
     */
    @SelectProvider(
            type = PapersDaoProvider.class,
            method = "selectBySearchChapter"
    )
    List<PapersSearchRspDto> selectBySearchChapter(PapersSearchByChapterReqDto reqDto);

    /**
     * 试卷库---同步测试--搜索--总数
     *
     * @param reqDto
     * @return
     */
    @SelectProvider(
            type = PapersDaoProvider.class,
            method = "selectCountBySearchChapter"
    )
    int selectCountBySearchChapter(PapersSearchByChapterReqDto reqDto);

    /**
     * 试卷库---升学考试--搜索--分页
     *
     * @param reqDto
     * @return
     */
    @SelectProvider(
            type = PapersDaoProvider.class,
            method = "selectBySearchGrade"
    )
    List<PapersSearchRspDto> selectBySearchGrade(PapersSearchByGradeReqDto reqDto);

    /**
     * 试卷库---升学考试--搜索--总数
     *
     * @param reqDto
     * @return
     */
    @SelectProvider(
            type = PapersDaoProvider.class,
            method = "selectCountBySearchGrade"
    )
    int selectCountBySearchGrade(PapersSearchByGradeReqDto reqDto);

    @SelectProvider(
            type = PapersDaoProvider.class,
            method = "selectGroupByStage"
    )
    List<ResourceStageSubjectCountDto> selectGroupByStage();

    @SelectProvider(
            type = PapersDaoProvider.class,
            method = "selectGroupByStageSubject"
    )
    List<ResourceStageSubjectCountDto> selectGroupByStageSubject();
    @SelectProvider(
            type = PapersDaoProvider.class,
            method = "selectGroupByProvince"
    )
    List<ResourceTypeCountDto> selectGroupByProvince();
    @SelectProvider(
            type = PapersDaoProvider.class,
            method = "selectGroupByPaperType"
    )
    List<ResourceTypeCountDto> selectGroupByPaperType();
    @SelectProvider(
            type = PapersDaoProvider.class,
            method = "selectGroupByStageGrade"
    )
    List<ResourceStageGradeCountDto> selectGroupByStageGrade();
}