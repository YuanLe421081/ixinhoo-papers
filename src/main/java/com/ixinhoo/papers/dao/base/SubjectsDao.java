package com.ixinhoo.papers.dao.base;

import com.chunecai.crumbs.api.dao.BaseDao;
import com.ixinhoo.config.redis.RedisCache;
import com.ixinhoo.papers.entity.base.Subjects;
import org.apache.ibatis.annotations.CacheNamespace;

//@CacheNamespace(implementation = RedisCache.class)
public interface SubjectsDao extends BaseDao<Subjects> {
}