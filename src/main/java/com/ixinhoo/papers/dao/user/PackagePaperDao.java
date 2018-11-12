package com.ixinhoo.papers.dao.user;

import com.chunecai.crumbs.api.dao.BaseDao;
import com.ixinhoo.config.redis.RedisCache;
import com.ixinhoo.papers.dto.count.ResourceStageGradeCountDto;
import com.ixinhoo.papers.dto.count.ResourceStageSubjectCountDto;
import com.ixinhoo.papers.dto.count.ResourceTypeCountDto;
import com.ixinhoo.papers.dto.count.TendencyDayCountDto;
import com.ixinhoo.papers.entity.user.PackagePaper;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

//@CacheNamespace(implementation = RedisCache.class)
public interface PackagePaperDao extends BaseDao<PackagePaper> {
    @SelectProvider(
            type = PackagePaperDaoProvider.class,
            method = "selectGroupByTypeAndTime"
    )
    List<TendencyDayCountDto> selectGroupByTypeAndTime(@Param("beginTime") Long beginTime,
                                                       @Param("endTime") Long endTime,
                                                       @Param("dayType") Integer dayType);
    @SelectProvider(
            type = PackagePaperDaoProvider.class,
            method = "selectGroupByTeach"
    )
    List<ResourceTypeCountDto> selectGroupByTeach();
    @SelectProvider(
            type = PackagePaperDaoProvider.class,
            method = "selectGroupByStage"
    )
    List<ResourceTypeCountDto> selectGroupByStage();
    @SelectProvider(
            type = PackagePaperDaoProvider.class,
            method = "selectGroupByStageSubject"
    )
    List<ResourceStageSubjectCountDto> selectGroupByStageSubject();
    @SelectProvider(
            type = PackagePaperDaoProvider.class,
            method = "selectGroupByProvince"
    )
    List<ResourceTypeCountDto> selectGroupByProvince();
    @SelectProvider(
            type = PackagePaperDaoProvider.class,
            method = "selectGroupByStageGrade"
    )
    List<ResourceStageGradeCountDto> selectGroupByStageGrade();
    @SelectProvider(
            type = PackagePaperDaoProvider.class,
            method = "selectGroupByHourAndTime"
    )
    List<TendencyDayCountDto> selectGroupByHourAndTime(@Param("beginTime") Long beginTime,
                                                       @Param("endTime") Long endTime);
}