package com.ixinhoo.papers.dao.user;

import com.chunecai.crumbs.api.dao.BaseDao;
import com.ixinhoo.config.redis.RedisCache;
import com.ixinhoo.papers.dto.count.TendencyDayCountDto;
import com.ixinhoo.papers.entity.user.User;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

//@CacheNamespace(implementation = RedisCache.class)
public interface UserDao extends BaseDao<User> {

    @SelectProvider(
            type = UserDaoProvider.class,
            method = "selectGroupByTypeAndTime"
    )
    List<TendencyDayCountDto> selectGroupByTypeAndTime(@Param("beginTime") Long beginTime,
                                                       @Param("endTime") Long endTime,
                                                       @Param("dayType") Integer dayType);

    @SelectProvider(
            type = UserDaoProvider.class,
            method = "selectGroupByHourAndTime"
    )
    List<TendencyDayCountDto> selectGroupByHourAndTime(@Param("beginTime") Long beginTime,
                                                       @Param("endTime") Long endTime);

    @SelectProvider(
            type = UserDaoProvider.class,
            method = "selectGroupByHourAndFirstPayTime"
    )
    List<TendencyDayCountDto> selectGroupByHourAndFirstPayTime(@Param("beginTime") Long beginTime,
                                                               @Param("endTime") Long endTime);
}