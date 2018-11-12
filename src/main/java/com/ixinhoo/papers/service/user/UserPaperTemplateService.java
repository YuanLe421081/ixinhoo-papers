package com.ixinhoo.papers.service.user;

import com.chunecai.crumbs.api.client.DetailDto;
import com.chunecai.crumbs.api.client.ListDto;
import com.chunecai.crumbs.api.client.StatusDto;
import com.chunecai.crumbs.api.dao.BaseDao;
import com.chunecai.crumbs.api.service.BaseService;
import com.chunecai.crumbs.code.util.collection.Collections3;
import com.chunecai.crumbs.code.util.mapper.BeanMapper;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ixinhoo.papers.dao.papers.QuestionDao;
import com.ixinhoo.papers.dao.papers.QuestionKnowledgeDao;
import com.ixinhoo.papers.dao.user.UserCollectionDao;
import com.ixinhoo.papers.dao.user.UserPaperTemplateDao;
import com.ixinhoo.papers.dao.user.UserPaperTemplateQuestionDao;
import com.ixinhoo.papers.dao.user.UserPaperTemplateTypeDao;
import com.ixinhoo.papers.dto.common.CommonIdAndNameDto;
import com.ixinhoo.papers.dto.papers.PapersQuestionContentDto;
import com.ixinhoo.papers.dto.papers.PapersQuestionDto;
import com.ixinhoo.papers.dto.papers.PapersQuestionTypeDto;
import com.ixinhoo.papers.dto.user.paper.UserPaperTemplateSaveDto;
import com.ixinhoo.papers.dto.user.paper.UserPaperTemplateSaveTypeDto;
import com.ixinhoo.papers.dto.user.paper.UserPaperTemplateSaveTypeKnowledgeDto;
import com.ixinhoo.papers.entity.EntitySetting;
import com.ixinhoo.papers.entity.papers.Question;
import com.ixinhoo.papers.entity.papers.QuestionKnowledge;
import com.ixinhoo.papers.entity.user.UserCollection;
import com.ixinhoo.papers.entity.user.UserPaperTemplate;
import com.ixinhoo.papers.entity.user.UserPaperTemplateQuestion;
import com.ixinhoo.papers.entity.user.UserPaperTemplateType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.weekend.WeekendSqls;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserPaperTemplateService extends BaseService<UserPaperTemplate> {
    @Autowired
    private UserPaperTemplateDao dao;
    @Autowired
    private UserPaperTemplateTypeDao templateTypeDao;
    @Autowired
    private UserPaperTemplateQuestionDao templateQuestionDao;
    @Autowired
    private QuestionDao questionDao;
    @Autowired
    private UserCollectionDao userCollectionDao;
    @Autowired
    private QuestionKnowledgeDao questionKnowledgeDao;

    @Override
    protected BaseDao getBaseDao() {
        return dao;
    }

    @Transactional
    public StatusDto saveUserPaperTemplate(UserPaperTemplateSaveDto reqDto) {
        StatusDto dto = new StatusDto(false);
        if (reqDto == null) {
            dto.setMsg("参数为空");
        } else {
            UserPaperTemplate userPaperTemplate = null;
            if (reqDto.getId() != null && reqDto.getId() != 0L) {//编辑
                userPaperTemplate = dao.selectByPrimaryKey(reqDto.getId());
            }
            if (userPaperTemplate == null) {
                userPaperTemplate = new UserPaperTemplate();
            }
            userPaperTemplate.setTime(System.currentTimeMillis());
            userPaperTemplate.setTitle(reqDto.getTitle());
            userPaperTemplate.setUserId(reqDto.getUserId());
            userPaperTemplate.setGrade(reqDto.getGrade());
            userPaperTemplate.setStage(reqDto.getStage());
            userPaperTemplate.setSubjectId(reqDto.getSubjectId());
            userPaperTemplate.setSubjectName(reqDto.getSubjectName());
            userPaperTemplate.setTerm(reqDto.getTerm());
            userPaperTemplate.setVersionId(reqDto.getVersionId());
            if (userPaperTemplate.getId() == null || userPaperTemplate.getId() == 0L) {
                dao.insert(userPaperTemplate);
            } else {
                dao.updateByPrimaryKey(userPaperTemplate);
                //删除重新添加
                templateTypeDao.deleteByExample(new Example.Builder(UserPaperTemplateType.class)
                        .where(WeekendSqls.<UserPaperTemplateType>custom()
                                .andEqualTo(UserPaperTemplateType::getTemplateId, userPaperTemplate.getId())).build());
                templateQuestionDao.deleteByExample(new Example.Builder(UserPaperTemplateQuestion.class)
                        .where(WeekendSqls.<UserPaperTemplateQuestion>custom()
                                .andEqualTo(UserPaperTemplateQuestion::getTemplateId, userPaperTemplate.getId())).build());
            }
            dto.setStatus(true);
            if (Collections3.isNotEmpty(reqDto.getTypes())) {
                for (int i = 0; i < reqDto.getTypes().size(); i++) {
                    UserPaperTemplateSaveTypeDto d = reqDto.getTypes().get(i);
                    if (Collections3.isNotEmpty(d.getQuestions())) {
                        UserPaperTemplateType type = new UserPaperTemplateType();
                        type.setSort(i + 1);
                        type.setTypeId(d.getId());
                        type.setTemplateId(userPaperTemplate.getId());
                        type.setQuestionNum(d.getQuestions().size());
                        type.setTypeName(d.getName());
                        templateTypeDao.insert(type);
                        for (int i1 = 0; i1 < d.getQuestions().size(); i1++) {
                            UserPaperTemplateSaveTypeKnowledgeDto d1 = d.getQuestions().get(i1);
                            UserPaperTemplateQuestion question = new UserPaperTemplateQuestion();
                            question.setTemplateId(userPaperTemplate.getId());
                            question.setTypeId(type.getId());
                            question.setSort(i1 + 1);
                            if (Collections3.isNotEmpty(d1.getKnowledge())) {
                                question.setKnowledgeIds(String.join(",", d1.getKnowledge().stream().map(d3 -> d3.getId().toString()).collect(Collectors.toList())));
                                question.setKnowledgeNames(String.join(",", d1.getKnowledge().stream().map(d3 -> d3.getName()).collect(Collectors.toList())));
                            }
                            question.setDifficult(d1.getDifficult());
                            templateQuestionDao.insert(question);
                        }
                    }
                }
            }
        }
        return dto;
    }

    public DetailDto<PapersQuestionDto> makeTemplatePapersQuestion(UserPaperTemplateSaveDto reqDto) {
        DetailDto<PapersQuestionDto> dto = new DetailDto<>(false);
        if (reqDto == null) {
            dto.setMsg("参数为空");
        } else {
            dto.setStatus(true);
            PapersQuestionDto detail = new PapersQuestionDto();
            detail.setTitle(reqDto.getTitle());
            List<PapersQuestionTypeDto> typeList = Lists.newArrayList();//类型集合
            List<Question> questionList = Lists.newArrayList();
            for (int i = 0; i < reqDto.getTypes().size(); i++) {
                UserPaperTemplateSaveTypeDto d = reqDto.getTypes().get(i);
                if (Collections3.isNotEmpty(d.getQuestions())) {
                    for (int i1 = 0; i1 < d.getQuestions().size(); i1++) {
                        UserPaperTemplateSaveTypeKnowledgeDto d1 = d.getQuestions().get(i1);
                        if (Collections3.isNotEmpty(d1.getKnowledge())) {
                            questionList.addAll(questionDao.selectByTypeIdAndKnowledgeAndNum(d.getId(), d1.getKnowledge().stream().map(d4 -> d4.getId()).collect(Collectors.toList()), d1.getDifficult(), 1));
                        }
                    }
                }
            }
            //将试题进行分类,整合返回数据给客户端
            if (Collections3.isNotEmpty(questionList)) {
                Map<Long, List<Long>> map = Maps.newHashMap();
                Map<Long, Question> questionMap = Maps.newHashMap();
                List<Long> questionIds = Lists.newArrayList();
                questionList.forEach(d -> {
                    questionIds.add(d.getId());
                    questionMap.put(d.getId(), d);
                    List<Long> list;
                    if (map.containsKey(d.getTypeId())) {
                        list = map.get(d.getTypeId());
                    } else {
                        list = Lists.newArrayList();
                    }
                    list.add(d.getId());
                    map.put(d.getTypeId(), list);
                });
                //查询用户收藏的试题
                List<UserCollection> userCollections = userCollectionDao.selectByExample(new Example.Builder(UserCollection.class)
                        .where(WeekendSqls.<UserCollection>custom().andIn(UserCollection::getDataId, questionIds)
                                .andEqualTo(UserCollection::getTypeId, EntitySetting.DATA_TYPE_QUESTION))
                        .build());
                List<Long> collectionQuestionIds = userCollections.stream().map(d -> d.getDataId()).collect(Collectors.toList());
                //查询试题关联
                List<QuestionKnowledge> knowledges = questionKnowledgeDao.selectByExample(new Example.Builder(QuestionKnowledge.class)
                        .where(WeekendSqls.<QuestionKnowledge>custom().andIn(QuestionKnowledge::getQuestionId, questionIds)).build());
                Map<Long, List<QuestionKnowledge>> knowledgeMap = Maps.newHashMap();
                knowledges.forEach(d -> {
                    List<QuestionKnowledge> list;
                    if (knowledgeMap.containsKey(d.getQuestionId())) {
                        list = knowledgeMap.get(d.getQuestionId());
                    } else {
                        list = Lists.newArrayList();
                    }
                    list.add(d);
                    knowledgeMap.put(d.getQuestionId(), list);
                });
                map.forEach((k, v) -> {
                    PapersQuestionTypeDto temp = new PapersQuestionTypeDto();
                    temp.setTypeId(k);
                    List<PapersQuestionContentDto> questions = Lists.newArrayList();
                    v.forEach(d -> {
                        if (questionMap.containsKey(d)) {
                            Question question = questionMap.get(d);
                            temp.setTypeName(question.getTypeName());
                            PapersQuestionContentDto pqc = BeanMapper.map(question, PapersQuestionContentDto.class);
                            if (collectionQuestionIds.contains(d)) {
                                pqc.setCollection(true);
                            }
                            if (knowledgeMap.containsKey(d)) {
                                List<QuestionKnowledge> questionKnowledgeList = knowledgeMap.get(d);
                                List<CommonIdAndNameDto> commonIdAndNameDtos = Lists.newArrayList();
                                questionKnowledgeList.forEach(d1 -> {
                                    CommonIdAndNameDto commonIdAndName = new CommonIdAndNameDto();
                                    commonIdAndName.setId(d1.getKnowledgeId());
                                    commonIdAndName.setName(d1.getKnowledgeName());
                                    commonIdAndNameDtos.add(commonIdAndName);
                                });
                                pqc.setKnowledge(commonIdAndNameDtos);
                            }
                            questions.add(pqc);
                        }
                        temp.setQuestions(questions);
                    });
                    typeList.add(temp);
                });
                detail.setTypeList(typeList);
                dto.setDetail(detail);
                dto.setStatus(true);
            } else {
                dto.setMsg("试题数据查询为空");
            }
        }
        return dto;
    }

    public DetailDto<UserPaperTemplateSaveDto> findUserPaperTemplateById(Long id) {
        DetailDto<UserPaperTemplateSaveDto> dto = new DetailDto<>(false);
        if (id == null || id == 0L) {
            dto.setMsg("id为空");
        } else {
            UserPaperTemplate userPaperTemplate = super.findById(id);
            if (userPaperTemplate == null) {
                dto.setMsg("数据不存在");
            } else {
                dto.setStatus(true);
                UserPaperTemplateSaveDto detail = BeanMapper.map(userPaperTemplate, UserPaperTemplateSaveDto.class);
                //查询分类
                List<UserPaperTemplateType> typeList = templateTypeDao.selectByExample(new Example.Builder(UserPaperTemplateType.class)
                        .where(WeekendSqls.<UserPaperTemplateType>custom()
                                .andEqualTo(UserPaperTemplateType::getTemplateId, userPaperTemplate.getId())).build());
                if (Collections3.isNotEmpty(typeList)) {
                    List<UserPaperTemplateSaveTypeDto> types = Lists.newArrayList();
                    typeList.sort((o1, o2) -> o1.getSort() - o2.getSort());
                    typeList.forEach(d -> {
                        UserPaperTemplateSaveTypeDto temp = new UserPaperTemplateSaveTypeDto();
                        temp.setId(d.getTypeId());
                        temp.setName(d.getTypeName());
                        List<UserPaperTemplateQuestion> questionList = templateQuestionDao.selectByExample(new Example.Builder(UserPaperTemplateQuestion.class)
                                .where(WeekendSqls.<UserPaperTemplateQuestion>custom()
                                        .andEqualTo(UserPaperTemplateQuestion::getTypeId, d.getId())
                                        .andEqualTo(UserPaperTemplateQuestion::getTemplateId, userPaperTemplate.getId())).build());
                        if (Collections3.isNotEmpty(questionList)) {
                            List<UserPaperTemplateSaveTypeKnowledgeDto> questions = Lists.newArrayList();
                            questionList.sort((o1, o2) -> o1.getSort() - o2.getSort());
                            questionList.forEach(d1 -> {
                                UserPaperTemplateSaveTypeKnowledgeDto temp1 = new UserPaperTemplateSaveTypeKnowledgeDto();
                                temp1.setDifficult(d1.getDifficult());
                                if (!Strings.isNullOrEmpty(d1.getKnowledgeIds())) {
                                    List<CommonIdAndNameDto> list1 = Lists.newArrayList();
                                    String[] k1 = d1.getKnowledgeIds().split(",");
                                    String[] k2 = d1.getKnowledgeNames().split(",");
                                    for (int i = 0; i < k1.length; i++) {
                                        CommonIdAndNameDto c = new CommonIdAndNameDto();
                                        c.setId(Long.parseLong(k1[i]));
                                        c.setName(k2[i]);
                                        list1.add(c);
                                    }
                                    temp1.setKnowledge(list1);
                                }
                                questions.add(temp1);
                            });
                            temp.setQuestions(questions);
                        }
                        types.add(temp);
                    });
                    detail.setTypes(types);
                }
                dto.setDetail(detail);
            }
        }
        return dto;
    }

    public ListDto<UserPaperTemplate> findByUserId(Long userId) {
        ListDto<UserPaperTemplate> dto = new ListDto<>(false);
        if (userId == null || userId == 0L) {
            dto.setMsg("用户为空");
        } else {
            dto.setList(dao.selectByExample(new Example.Builder(UserPaperTemplate.class)
                    .where(WeekendSqls.<UserPaperTemplate>custom()
                            .andEqualTo(UserPaperTemplate::getUserId, userId)).build()));
            dto.setStatus(true);
        }
        return dto;
    }
}