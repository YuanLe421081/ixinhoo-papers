package com.ixinhoo.papers.dao.papers;

import com.chunecai.crumbs.api.dao.BaseDao;
import com.ixinhoo.config.redis.RedisCache;
import com.ixinhoo.papers.entity.papers.QuestionType;
import org.apache.ibatis.annotations.CacheNamespace;

//@CacheNamespace(implementation = RedisCache.class)
public interface QuestionTypeDao extends BaseDao<QuestionType> {
}