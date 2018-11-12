package com.ixinhoo.papers.dao.authority;

import com.chunecai.crumbs.api.dao.BaseDao;
import com.ixinhoo.config.redis.RedisCache;
import com.ixinhoo.papers.entity.authority.SystemUser;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.mapping.StatementType;

import java.util.HashMap;
import java.util.Map;
//@CacheNamespace(implementation = RedisCache.class)
public interface SystemUserDao extends BaseDao<SystemUser> {
    /**
     * 执行存储过程select_user_by_id
     */
    @SuppressWarnings("rawtypes")
    @Select("call select_user_by_id(#{id,mode=IN},#{userName,mode=OUT,jdbcType=VARCHAR},#{userCode,mode=OUT,jdbcType=VARCHAR})")
    @Options(statementType= StatementType.CALLABLE )
    @ResultType(Map.class)
    public Object callGenCiPropertyValue(Map map);
}