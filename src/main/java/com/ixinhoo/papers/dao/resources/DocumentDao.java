package com.ixinhoo.papers.dao.resources;

import com.chunecai.crumbs.api.dao.BaseDao;
import com.ixinhoo.config.redis.RedisCache;
import com.ixinhoo.papers.dto.count.ResourceStageGradeCountDto;
import com.ixinhoo.papers.dto.count.ResourceStageSubjectCountDto;
import com.ixinhoo.papers.dto.count.ResourceTypeCountDto;
import com.ixinhoo.papers.dto.resources.DocumentSearchReqDto;
import com.ixinhoo.papers.dto.resources.DocumentSearchRspDto;
import com.ixinhoo.papers.entity.resources.Document;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

//@CacheNamespace(implementation = RedisCache.class)
public interface DocumentDao extends BaseDao<Document> {
    /**
     * 首页推荐--精品推荐
     *
     * @param grade
     * @param subjectId
     * @param provinceId
     * @param userId
     * @param upHalf
     * @return
     */
    @SelectProvider(
            type = DocumentDaoProvider.class,
            method = "selectByHomeRecommend"
    )
    List<Document> selectByHomeRecommend(@Param("grade") Integer grade, @Param("subjectId") Long subjectId, @Param("provinceId") Long provinceId, @Param("userId") Long userId, @Param("upHalf") Boolean upHalf);

    /**
     * 备课列表搜索--列表
     *
     * @param reqDto
     * @return
     */
    @SelectProvider(
            type = DocumentDaoProvider.class,
            method = "selectBySearch"
    )
    List<DocumentSearchRspDto> selectBySearch(DocumentSearchReqDto reqDto);

    /**
     * 备课列表搜索--总数
     *
     * @param reqDto
     * @return
     */
    @SelectProvider(
            type = DocumentDaoProvider.class,
            method = "selectCountBySearch"
    )
    Integer selectCountBySearch(DocumentSearchReqDto reqDto);


    /**
     * 同类资料--预览资料详情页-右边推荐
     *
     * @return
     */
    @SelectProvider(
            type = DocumentDaoProvider.class,
            method = "selectSameTypeById"
    )
    List<DocumentSearchRspDto> selectSameTypeById(@Param("chapterIds") List<Long> chapterIds,
                                                  @Param("versionId") Long versionId,
                                                  @Param("typeId") Integer typeId,
                                                  @Param("hasCoin") Boolean hasCoin);

    /**
     * 猜你喜欢--预览资料详情页-底部推荐
     *
     * @return
     */
    @SelectProvider(
            type = DocumentDaoProvider.class,
            method = "selectGuessByIdAndPage"
    )
    List<DocumentSearchRspDto> selectGuessByIdAndPage(@Param("chapterIds") List<Long> chapterIds,
                                                      @Param("versionId") Long versionId,
                                                      @Param("typeId") Integer typeId,
                                                      @Param("p") Integer p,
                                                      @Param("s") Integer s);

    /**
     * 下载成功--资料推荐
     *
     * @return
     */
    @SelectProvider(
            type = DocumentDaoProvider.class,
            method = "selectDownloadRecommendByIdAndPage"
    )
    List<DocumentSearchRspDto> selectDownloadRecommendByIdAndPage(@Param("chapterIds") List<Long> chapterIds,
                                                                  @Param("versionIds") List<Long> versionIds,
                                                                  @Param("typeId") Integer typeId,
                                                                  @Param("p") Integer p,
                                                                  @Param("s") Integer s);

    /**
     * 类型分组查询
     *
     * @return
     */
    @SelectProvider(
            type = DocumentDaoProvider.class,
            method = "selectGroupByType"
    )
    List<ResourceTypeCountDto> selectGroupByType();

    /**
     * 学段分组查询
     *
     * @return
     */
    @SelectProvider(
            type = DocumentDaoProvider.class,
            method = "selectGroupByStage"
    )
    List<ResourceTypeCountDto> selectGroupByStage();

    /**
     * 学段学科分组查询
     *
     * @return
     */
    @SelectProvider(
            type = DocumentDaoProvider.class,
            method = "selectGroupByStageSubject"
    )
    List<ResourceStageSubjectCountDto> selectGroupByStageSubject();

    /**
     * 地区分组查询
     *
     * @return
     */
    @SelectProvider(
            type = DocumentDaoProvider.class,
            method = "selectGroupByProvince"
    )
    List<ResourceTypeCountDto> selectGroupByProvince();
    /**
     * 年级查询
     *
     * @return
     */
    @SelectProvider(
            type = DocumentDaoProvider.class,
            method = "selectGroupByStageGrade"
    )
    List<ResourceStageGradeCountDto> selectGroupByStageGrade();
}