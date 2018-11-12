package com.ixinhoo.papers.dao.papers;

import com.chunecai.crumbs.api.dao.BaseDao;
import com.ixinhoo.config.redis.RedisCache;
import com.ixinhoo.papers.dto.QuestionTestDto;
import com.ixinhoo.papers.dto.count.ResourceStageGradeCountDto;
import com.ixinhoo.papers.dto.count.ResourceStageSubjectCountDto;
import com.ixinhoo.papers.dto.count.ResourceTypeCountDto;
import com.ixinhoo.papers.dto.papers.QuestionSearchReqDto;
import com.ixinhoo.papers.dto.papers.QuestionSearchRspDto;
import com.ixinhoo.papers.dto.papers.QuestionTypeDto;
import com.ixinhoo.papers.entity.papers.Question;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.Collection;
import java.util.List;
//@CacheNamespace(implementation = RedisCache.class)
public interface QuestionDao extends BaseDao<Question> {
    /**
     * 列表搜索--列表
     *
     * @param reqDto
     * @return
     */
    @SelectProvider(
            type = QuestionDaoProvider.class,
            method = "selectBySearch"
    )
    List<QuestionSearchRspDto> selectBySearch(QuestionSearchReqDto reqDto);

    /**
     * 列表搜索--总数
     *
     * @param reqDto
     * @return
     */
    @SelectProvider(
            type = QuestionDaoProvider.class,
            method = "selectCountBySearch"
    )
    Integer selectCountBySearch(QuestionSearchReqDto reqDto);
    /**
     * 根据章节主键id分组查询题型和题目总数
     * @return
     */
    @SelectProvider(
            type = QuestionDaoProvider.class,
            method = "selectCountByChapterIdsGroupByType"
    )
    List<QuestionTypeDto> selectCountByChapterIdsGroupByType(@Param("ids")List<Long> ids, @Param("other") Boolean other) ;

    @SelectProvider(
            type = QuestionDaoProvider.class,
            method = "selectByTypeIdAndChapterAndNum"
    )
    List<Question> selectByTypeIdAndChapterAndNum(@Param("typeId")Long typeId, @Param("chapterIds")List<Long> chapterIds,@Param("difficult") Integer difficult, @Param("num")Integer num) ;
    @SelectProvider(
            type = QuestionDaoProvider.class,
            method = "selectByTypeIdAndKnowledgeAndNum"
    )
    List<Question> selectByTypeIdAndKnowledgeAndNum(@Param("typeId")Long typeId, @Param("knowledgeIds")List<Long> knowledgeIds,@Param("difficult") Integer difficult, @Param("num")Integer num) ;
    @SelectProvider(
            type = QuestionDaoProvider.class,
            method = "selectGroupByDifficult"
    )
    List<ResourceTypeCountDto> selectGroupByDifficult();
    @SelectProvider(
            type = QuestionDaoProvider.class,
            method = "selectGroupByStage"
    )
    List<ResourceTypeCountDto> selectGroupByStage();
    @SelectProvider(
            type = QuestionDaoProvider.class,
            method = "selectGroupByStageGrade"
    )
    List<ResourceStageGradeCountDto> selectGroupByStageGrade();
    @SelectProvider(
            type = QuestionDaoProvider.class,
            method = "selectGroupByStageSubject"
    )
    List<ResourceStageSubjectCountDto> selectGroupByStageSubject();
}