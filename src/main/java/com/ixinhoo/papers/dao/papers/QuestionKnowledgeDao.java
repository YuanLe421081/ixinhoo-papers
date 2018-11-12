package com.ixinhoo.papers.dao.papers;

import com.chunecai.crumbs.api.dao.BaseDao;
import com.ixinhoo.config.redis.RedisCache;
import com.ixinhoo.papers.dto.papers.PaperTemplateReqDto;
import com.ixinhoo.papers.entity.papers.QuestionKnowledge;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;
//@CacheNamespace(implementation = RedisCache.class)
public interface QuestionKnowledgeDao extends BaseDao<QuestionKnowledge> {
    @SelectProvider(
            type = QuestionKnowledgeDaoProvider.class,
            method = "selectByQuestionType"
    )
    List<QuestionKnowledge> selectByQuestionType(PaperTemplateReqDto reqDto);
}