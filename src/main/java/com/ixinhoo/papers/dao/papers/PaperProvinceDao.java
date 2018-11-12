package com.ixinhoo.papers.dao.papers;

import com.chunecai.crumbs.api.dao.BaseDao;
import com.ixinhoo.config.redis.RedisCache;
import com.ixinhoo.papers.entity.papers.PaperProvince;
import org.apache.ibatis.annotations.CacheNamespace;

//@CacheNamespace(implementation = RedisCache.class)
public interface PaperProvinceDao extends BaseDao<PaperProvince> {
}