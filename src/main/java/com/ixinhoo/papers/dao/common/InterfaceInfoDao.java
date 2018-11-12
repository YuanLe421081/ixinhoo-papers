package com.ixinhoo.papers.dao.common;

import com.chunecai.crumbs.api.dao.BaseDao;
import com.ixinhoo.config.redis.RedisCache;
import com.ixinhoo.papers.entity.common.InterfaceInfo;
import org.apache.ibatis.annotations.CacheNamespace;

//@CacheNamespace(implementation = RedisCache.class)
public interface InterfaceInfoDao extends BaseDao<InterfaceInfo> {
}