package com.ixinhoo.papers.dao.user;

import com.chunecai.crumbs.api.dao.BaseDao;
import com.ixinhoo.papers.dto.count.TendencyDayCountDto;
import com.ixinhoo.papers.entity.user.UserLoginRecord;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

public interface UserLoginRecordDao extends BaseDao<UserLoginRecord> {
    @SelectProvider(
            type = UserLoginRecordDaoProvider.class,
            method = "selectGroupByTypeAndTime"
    )
    List<TendencyDayCountDto> selectGroupByTypeAndTime(@Param("beginTime") Long beginTime,
                                                       @Param("endTime") Long endTime,
                                                       @Param("dayType") Integer dayType);
    @SelectProvider(
            type = UserLoginRecordDaoProvider.class,
            method = "selectGroupByHourAndTime"
    )
    List<TendencyDayCountDto> selectGroupByHourAndTime(@Param("beginTime") Long beginTime,
                                                       @Param("endTime") Long endTime);
}