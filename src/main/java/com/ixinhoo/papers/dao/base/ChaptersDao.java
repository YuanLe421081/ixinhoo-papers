package com.ixinhoo.papers.dao.base;

import com.chunecai.crumbs.api.dao.BaseDao;
import com.ixinhoo.config.redis.RedisCache;
import com.ixinhoo.papers.entity.base.Chapters;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;
//@CacheNamespace(implementation = RedisCache.class)
public interface ChaptersDao extends BaseDao<Chapters> {

	@SelectProvider(type = ChaptersDaoProvider.class,
					method = "findByChapter")
	List<Chapters> findByChapter(@Param("stage") Integer stage,@Param("subjectId") Long subjectId);

}