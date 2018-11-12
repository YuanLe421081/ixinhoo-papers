package com.ixinhoo.papers.service.papers;

import com.chunecai.crumbs.api.client.DetailDto;
import com.chunecai.crumbs.api.dao.BaseDao;
import com.chunecai.crumbs.api.service.BaseService;
import com.chunecai.crumbs.code.util.mapper.BeanMapper;
import com.chunecai.crumbs.code.util.string.StringUtil;
import com.ixinhoo.papers.dao.papers.QuestionAnswerDao;
import com.ixinhoo.papers.dao.papers.QuestionKnowledgeDao;
import com.ixinhoo.papers.dto.papers.QuestionAnswerDto;
import com.ixinhoo.papers.entity.papers.QuestionAnswer;
import com.ixinhoo.papers.entity.papers.QuestionKnowledge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.weekend.WeekendSqls;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionAnswerService extends BaseService<QuestionAnswer> {
    @Autowired
    private QuestionAnswerDao dao;
    @Autowired
    private QuestionKnowledgeDao questionKnowledgeDao;

    @Override
    protected BaseDao getBaseDao() {
        return dao;
    }

    public DetailDto<QuestionAnswerDto> findDetailByQuestionId(Long id) {
        DetailDto<QuestionAnswerDto> dto = new DetailDto<>(false);
        if (id == null || id == 0L) {
            dto.setMsg("参数为空");
        } else {
            QuestionAnswer questionAnswer = new QuestionAnswer();
            questionAnswer.setQuestionId(id);
            questionAnswer = dao.selectOne(questionAnswer);
            if (questionAnswer == null) {
                dto.setMsg("该题目没有答案");
            } else {
                dto.setStatus(true);
                QuestionAnswerDto detail = BeanMapper.map(questionAnswer, QuestionAnswerDto.class);
                //查询试题下面的考点，考点即为知识点
                //知识点
                List<QuestionKnowledge> questionKnowledges = questionKnowledgeDao.selectByExample(new Example.Builder(QuestionKnowledge.class)
                        .where(WeekendSqls.<QuestionKnowledge>custom().andEqualTo(QuestionKnowledge::getQuestionId, id)).build());
                List<String> konwledges = questionKnowledges.stream().map(d -> d.getKnowledgeName()).collect(Collectors.toList());
                detail.setExamPoint(StringUtil.listJoinChart(konwledges, ","));
                dto.setDetail(detail);
            }
        }
        return dto;
    }
}