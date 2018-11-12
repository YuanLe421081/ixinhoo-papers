package com.ixinhoo.papers.dao.common;

import com.chunecai.crumbs.api.dao.BaseDao;
import com.ixinhoo.config.redis.RedisCache;
import com.ixinhoo.papers.entity.common.MessageNotice;
import org.apache.ibatis.annotations.*;

import java.util.List;
//@CacheNamespace(implementation = RedisCache.class)
public interface MessageNoticeDao extends BaseDao<MessageNotice> {
    /**
     * 用户消息
     *
     * @return
     */
    @SelectProvider(
            type = MessageNoticeProvider.class,
            method = "selectByUserAndRoleDataCode"
    )
    @Results({
            @Result(id=true,column="id",property="id"),
            @Result(column="title",property="title"),
            @Result(column="content",property="content"),
            @Result(column="type",property="type"),
            @Result(column="push_type",property="pushType"),
            @Result(column="data_code",property="dataCode"),
            @Result(column="push_time",property="pushTime"),
    })
    List<MessageNotice> selectByUserAndRoleDataCode(@Param("userCode") String userCode, @Param("roleCodes")List<String> roleCodes) ;
}