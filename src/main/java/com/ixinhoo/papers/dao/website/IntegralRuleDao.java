package com.ixinhoo.papers.dao.website;

import com.chunecai.crumbs.api.dao.BaseDao;
import com.ixinhoo.config.redis.RedisCache;
import com.ixinhoo.papers.entity.website.IntegralRule;
import org.apache.ibatis.annotations.CacheNamespace;

//@CacheNamespace(implementation = RedisCache.class)
public interface IntegralRuleDao extends BaseDao<IntegralRule> {
}