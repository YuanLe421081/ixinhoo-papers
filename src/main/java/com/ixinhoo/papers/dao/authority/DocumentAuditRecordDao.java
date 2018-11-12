package com.ixinhoo.papers.dao.authority;

import com.chunecai.crumbs.api.dao.BaseDao;
import com.ixinhoo.config.redis.RedisCache;
import com.ixinhoo.papers.entity.authority.DocumentAuditRecord;
import org.apache.ibatis.annotations.CacheNamespace;

//@CacheNamespace(implementation = RedisCache.class)
public interface DocumentAuditRecordDao extends BaseDao<DocumentAuditRecord> {
}