package com.ixinhoo.papers.dao.common;

import com.chunecai.crumbs.api.dao.BaseDao;
import com.ixinhoo.config.redis.RedisCache;
import com.ixinhoo.papers.entity.common.UserFeedback;
import org.apache.ibatis.annotations.CacheNamespace;

//@CacheNamespace(implementation = RedisCache.class)
public interface UserFeedbackDao extends BaseDao<UserFeedback> {
}