package com.ixinhoo.papers.dao.authority;

import com.chunecai.crumbs.api.dao.BaseDao;
import com.ixinhoo.config.redis.RedisCache;
import com.ixinhoo.papers.entity.authority.BackgroundMenu;
import org.apache.ibatis.annotations.CacheNamespace;
//@CacheNamespace(implementation = RedisCache.class)
public interface BackgroundMenuDao extends BaseDao<BackgroundMenu> {
}