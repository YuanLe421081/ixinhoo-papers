package com.ixinhoo.papers.dao.base;

import com.chunecai.crumbs.api.dao.BaseDao;
import com.ixinhoo.config.redis.RedisCache;
import com.ixinhoo.papers.entity.base.StageSubjects;
import org.apache.ibatis.annotations.CacheNamespace;

//@CacheNamespace(implementation = RedisCache.class)
public interface StageSubjectsDao extends BaseDao<StageSubjects> {
}