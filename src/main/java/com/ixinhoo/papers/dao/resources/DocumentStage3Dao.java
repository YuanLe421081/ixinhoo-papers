package com.ixinhoo.papers.dao.resources;

import com.chunecai.crumbs.api.dao.BaseDao;
import com.ixinhoo.config.redis.RedisCache;
import com.ixinhoo.papers.entity.resources.DocumentStage3;
import org.apache.ibatis.annotations.CacheNamespace;

//@CacheNamespace(implementation = RedisCache.class)
public interface DocumentStage3Dao extends BaseDao<DocumentStage3> {
}