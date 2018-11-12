package com.ixinhoo.papers.dao.user;

import com.chunecai.crumbs.api.dao.BaseDao;
import com.ixinhoo.config.redis.RedisCache;
import com.ixinhoo.papers.entity.user.UserPaperTemplateType;
import org.apache.ibatis.annotations.CacheNamespace;

//@CacheNamespace(implementation = RedisCache.class)
public interface UserPaperTemplateTypeDao extends BaseDao<UserPaperTemplateType> {
}