package com.ixinhoo.papers.dao.resources;

import com.chunecai.crumbs.api.dao.BaseDao;
import com.ixinhoo.config.redis.RedisCache;
import com.ixinhoo.papers.entity.resources.DocumentStage1;
import org.apache.ibatis.annotations.CacheNamespace;

//@CacheNamespace(implementation = RedisCache.class)
public interface DocumentStage1Dao extends BaseDao<DocumentStage1> {
}