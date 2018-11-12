package com.ixinhoo.papers.dao.user;

import com.chunecai.crumbs.api.dao.BaseDao;
import com.ixinhoo.config.redis.RedisCache;
import com.ixinhoo.papers.dto.count.*;
import com.ixinhoo.papers.entity.user.UserOrder;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.ArrayList;
import java.util.List;
//@CacheNamespace(implementation = RedisCache.class)
public interface UserOrderDao extends BaseDao<UserOrder> {
    @SelectProvider(
            type = UserOrderDaoProvider.class,
            method = "selectCountByAddUser"
    )
    int selectCountByAddUser(@Param("beginTime") Long beginTime,
                             @Param("endTime")Long endTime) ;

    @SelectProvider(
            type = UserOrderDaoProvider.class,
            method = "selectSumMoneyInType"
    )
    Double selectSumMoneyInType(@Param("beginTime") Long beginTime,
                                @Param("endTime")Long endTime,
                                @Param("type")List<Integer> type);
    @SelectProvider(
            type = UserOrderDaoProvider.class,
            method = "selectSumCoinInType"
    )
    Double selectSumCoinInType(@Param("beginTime") Long beginTime,
                                @Param("endTime")Long endTime,
                                @Param("type")List<Integer> type);
    @SelectProvider(
            type = UserOrderDaoProvider.class,
            method = "selectGroupByTypeAndTime"
    )
    List<TendencyDayCountDto> selectGroupByTypeAndTime(@Param("beginTime") Long beginTime,
                                                       @Param("endTime") Long endTime,
                                                       @Param("dayType") Integer dayType);

    @SelectProvider(
            type = UserOrderDaoProvider.class,
            method = "selectSumCoinByLessTime"
    )
    Double selectSumCoinByLessTime(@Param("beginTime")Long beginTime);
    @SelectProvider(
            type = UserOrderDaoProvider.class,
            method = "selectSumGroupByTypeAndTime"
    )
    List<TendencyDaySumDto> selectSumGroupByTypeAndTime(@Param("beginTime") Long beginTime,
                                                        @Param("endTime") Long endTime,
                                                        @Param("dayType") Integer dayType);
    @SelectProvider(
            type = UserOrderDaoProvider.class,
            method = "selectSumGroupByDocumentType"
    )
    List<ResourceTypeSumDto> selectSumGroupByDocumentType();
    @SelectProvider(
            type = UserOrderDaoProvider.class,
            method = "selectSumGroupByDocumentStage"
    )
    List<ResourceTypeSumDto> selectSumGroupByDocumentStage();
    @SelectProvider(
            type = UserOrderDaoProvider.class,
            method = "selectSumGroupByUserTeach"
    )
    List<ResourceTypeSumDto> selectSumGroupByUserTeach();
    @SelectProvider(
            type = UserOrderDaoProvider.class,
            method = "selectSumGroupByDocumentStageSubject"
    )
    List<ResourceStageSubjectSumDto> selectSumGroupByDocumentStageSubject();
    @SelectProvider(
            type = UserOrderDaoProvider.class,
            method = "selectSumGroupByDocumentProvince"
    )
    List<ResourceTypeSumDto> selectSumGroupByDocumentProvince();
    @SelectProvider(
            type = UserOrderDaoProvider.class,
            method = "selectSumRechargePriceLessTime"
    )
    Double selectSumRechargePriceLessTime(@Param("beginTime")Long beginTime);
    @SelectProvider(
            type = UserOrderDaoProvider.class,
            method = "selectSumPriceGroupByTypeAndTime"
    )

    List<TendencyDaySumDto> selectSumPriceGroupByTypeAndTime(@Param("beginTime") Long beginTime,
                                                             @Param("endTime") Long endTime,
                                                             @Param("dayType") Integer dayType);
    @SelectProvider(
            type = UserOrderDaoProvider.class,
            method = "selectSumRechargePriceByPrice"
    )
    Double selectSumRechargePriceByPrice(@Param("beginPrice") Double beginPrice,
                                         @Param("endPrice")  Double endPrice);

    @SelectProvider(
            type = UserOrderDaoProvider.class,
            method = "selectSumCoinGroupByHourAndTime"
    )
    List<TendencyDayCountDto> selectSumCoinGroupByHourAndTime(@Param("beginTime") Long beginTime,
                                                              @Param("endTime") Long endTime,
                                                              @Param("type") List<Integer> type);
    @SelectProvider(
            type = UserOrderDaoProvider.class,
            method = "selectSumMoneyGroupByHourAndTime"
    )
    List<TendencyDayCountDto> selectSumMoneyGroupByHourAndTime(@Param("beginTime") Long beginTime,
                                                               @Param("endTime") Long endTime);
    @SelectProvider(
            type = UserOrderDaoProvider.class,
            method = "selectSumLessMoneyGroupByHourAndTime"
    )
    List<TendencyDayCountDto> selectSumLessMoneyGroupByHourAndTime(@Param("beginTime") Long beginTime,
                                                                   @Param("endTime") Long endTime);
    @SelectProvider(
            type = UserOrderDaoProvider.class,
            method = "selectGroupByStageGrade"
    )
    List<ResourceStageGradeSumDto> selectGroupByStageGrade();

}