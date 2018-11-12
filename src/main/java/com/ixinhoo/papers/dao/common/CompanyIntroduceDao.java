package com.ixinhoo.papers.dao.common;

import com.chunecai.crumbs.api.dao.BaseDao;
import com.ixinhoo.papers.entity.common.CompanyIntroduce;
import org.apache.ibatis.annotations.CacheNamespace;

@CacheNamespace
public interface CompanyIntroduceDao extends BaseDao<CompanyIntroduce> {
}