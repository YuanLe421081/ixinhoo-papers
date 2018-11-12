package com.ixinhoo.papers.service.papers;

import com.chunecai.crumbs.api.client.DetailDto;
import com.chunecai.crumbs.api.client.ListDto;
import com.chunecai.crumbs.api.client.PageDto;
import com.chunecai.crumbs.api.client.StatusDto;
import com.chunecai.crumbs.api.dao.BaseDao;
import com.chunecai.crumbs.api.service.BaseService;
import com.chunecai.crumbs.api.shiro.ShiroUser;
import com.chunecai.crumbs.code.util.collection.Collections3;
import com.chunecai.crumbs.code.util.date.DateUtil;
import com.chunecai.crumbs.code.util.mapper.BeanMapper;
import com.chunecai.crumbs.code.util.mapper.JsonMapper;
import com.cnjy21.api.service.APIQuestionService;
import com.cnjy21.api.service.APIRequestParams;
import com.cnjy21.api.util.APIResult;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ixinhoo.papers.dao.base.ChaptersDao;
import com.ixinhoo.papers.dao.common.InterfaceInfoDao;
import com.ixinhoo.papers.dao.papers.*;
import com.ixinhoo.papers.dao.user.UserCollectionDao;
import com.ixinhoo.papers.dto.QuestionTestDto;
import com.ixinhoo.papers.dto.common.CommonIdAndNameDto;
import com.ixinhoo.papers.dto.papers.*;
import com.ixinhoo.papers.entity.EntitySetting;
import com.ixinhoo.papers.entity.base.Chapters;
import com.ixinhoo.papers.entity.common.InterfaceInfo;
import com.ixinhoo.papers.entity.papers.*;
import com.ixinhoo.papers.entity.user.UserCollection;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.weekend.WeekendSqls;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class QuestionService extends BaseService<Question> {

    private static Logger logger = LoggerFactory.getLogger(QuestionService.class);
    @Autowired
    private QuestionDao dao;
    @Autowired
    private InterfaceInfoDao interfaceInfoDao;
    @Autowired
    private QuestionChapterDao questionChapterDao;
    @Autowired
    private QuestionKnowledgeDao questionKnowledgeDao;
    @Autowired
    private QuestionExtendDao questionExtendDao;
    @Autowired
    private QuestionAnswerDao questionAnswerDao;
    @Autowired
    private ChaptersDao chaptersDao;
    @Autowired
    private PapersDao papersDao;
    @Autowired
    private UserCollectionDao userCollectionDao;

    @Override
    protected BaseDao getBaseDao() {
        return dao;
    }

    /**
     * 同步试题--id与21一致
     *
     * @return
     */
    @Transactional
    public StatusDto syncQuestion() {
        logger.error("试题同步开始==========");
        StatusDto dto = new StatusDto(false);
        APIQuestionService service = new APIQuestionService();
        //需要查询原来的数据进行增量更新，
        List<Question> dbPapers = dao.selectAll();
        List<String> codeList = dbPapers.stream().map(d -> d.getCode()).collect(Collectors.toList());
        //查询章节&备考信息进行题目获取
        List<Integer> types = Lists.newArrayList();
        types.add(EntitySetting.CHAPTERS_TYPE_CHAPTER);
        types.add(EntitySetting.CHAPTERS_TYPE_EXAM);
        List<Chapters> chaptersList = chaptersDao.selectByExample(new Example.Builder(Chapters.class)
                .where(WeekendSqls.<Chapters>custom().andIn(Chapters::getType, types))
                .build());
        if (Collections3.isEmpty(chaptersList)) {
            dto.setMsg("章节信息为空,请先同步章节信息");
        } else {
            //根据章节获取题目
            if (EntitySetting.TEST_ENV) {
                syncQuestionAndAnswer(codeList, service, chaptersList.get(0), EntitySetting.INTERFACE_FIRST_PAGE, EntitySetting.INTERFACE_MAX_PERPAGE);
                int size = codeList.size();
                for (Chapters d : chaptersList) {
                    codeList = syncQuestionAndAnswer(codeList, service, d, EntitySetting.INTERFACE_FIRST_PAGE, EntitySetting.INTERFACE_MAX_PERPAGE);
                    if (codeList.size() != size) {
                        break;
                    }
                }
            } else {
                for (Chapters d : chaptersList) {
                    codeList = syncQuestionAndAnswer(codeList, service, d, EntitySetting.INTERFACE_FIRST_PAGE, EntitySetting.INTERFACE_MAX_PERPAGE);
                }
            }
            dto.setStatus(true);
        }
        logger.error("试题同步结束==========");
        return dto;
    }

    @Transactional
    private List<String> syncQuestionAndAnswer(List<String> codeList, APIQuestionService service, Chapters chapters, int page, int perPage) {
        if (!Strings.isNullOrEmpty(chapters.getCode()) && chapters.getCode().matches("[\\d]+")) {
            APIRequestParams apiRequestParams = new APIRequestParams();
            apiRequestParams.page = page;
            apiRequestParams.perPage = perPage;
            apiRequestParams.chapterId = Integer.parseInt(chapters.getCode());
            apiRequestParams.stage = chapters.getStage();
            apiRequestParams.subjectId = chapters.getSubjectId().intValue();
            APIResult<com.cnjy21.api.model.question.Question> result = service.getQuestions(apiRequestParams);
            if (result != null && Collections3.isNotEmpty(result.getData())) {
                InterfaceInfo interfaceInfo = new InterfaceInfo();
                interfaceInfo.setTime(System.currentTimeMillis());
                interfaceInfo.setType(EntitySetting.INTERFACE_QUESTION);
                interfaceInfo.setReqData(JsonMapper.nonDefaultMapper().toJson(apiRequestParams));
                interfaceInfo.setRspData(JsonMapper.nonDefaultMapper().toJson(result));
//                ShiroUser user = UsersUtil.ShiroUser();
                ShiroUser user = null;
                if (user != null) {
                    interfaceInfo.setCreateId(user.getId());
                    interfaceInfo.setUpdateId(user.getId());
                }
                interfaceInfo.setCreateTime(System.currentTimeMillis());
                interfaceInfo.setUpdateTime(System.currentTimeMillis());
                interfaceInfoDao.insert(interfaceInfo);
                //判断页数与返回的数据是否一致循环获取试卷列表
                List<Question> list = Lists.newArrayList();
                List<QuestionExtend> extendList = Lists.newArrayList();
                List<QuestionChapter> questionChapterList = Lists.newArrayList();
                List<QuestionKnowledge> questionKnowledgeList = Lists.newArrayList();
                List<Long> questionIds = Lists.newArrayList();
                for (com.cnjy21.api.model.question.Question d : result.getData()) {
                    if (!codeList.contains(d.getQuestionId().toString())) {
                        questionIds.add(d.getQuestionId());
                        Question temp = new Question();
                        temp.setCode(d.getQuestionId().toString());
                        temp.setId(d.getQuestionId());
                        if (d.getSubjectId() != null) {
                            temp.setSubjectId(d.getSubjectId().longValue());
                        }
                        temp.setAuditStatus(EntitySetting.COMMON_SUCCESS);
//                        if (Strings.isNullOrEmpty(d.getCreatedAt())) {
//                            Date date = DateUtil.getInstance().parseDate(d.getCreatedAt(), "yyyy-MM-dd HH:mm:ss");
//                            temp.setCreatedAt(date.getTime());
//                        }
                        temp.setStage(d.getStage());
                        temp.setContent(d.getStem());
                        temp.setTypeId(d.getType().longValue());
                        temp.setTypeName(d.getTypeName());
                        temp.setAuditStatus(EntitySetting.COMMON_SUCCESS);
                        if (d.getPaperId() != null) {
                            temp.setPaperId(d.getPaperId().longValue());
                        }
                        if (d.getParentId() != null) {
                            temp.setSupId(d.getPaperId().longValue());
                        }
                        temp.setBaseType(d.getBaseType());
                        temp.setBaseTypeName(d.getBaseTypeName());
                        temp.setDifficult(d.getDifficult());
                        temp.setDifficultName(d.getDifficultName());
                        temp.setExamType(d.getExamType());
                        temp.setExamTypeName(d.getExamTypeName());
                        temp.setGrade(d.getGrade());
                        temp.setSource(d.getSource());
                        if (Collections3.isNotEmpty(d.getOptions())) {
                            temp.setOptions(JsonMapper.nonDefaultMapper().toJson(d.getOptions()));
                        }
                        temp.setStatus(EntitySetting.COMMON_SUCCESS);
                        if (Collections3.isEmpty(d.getKnowledge())) {
                            temp.setKnowledgeNum(0);
                        } else {
                            temp.setKnowledgeNum(d.getKnowledge().size());
                            d.getKnowledge().forEach(d1 -> {
                                QuestionKnowledge questionKnowledge = new QuestionKnowledge();
                                questionKnowledge.setKnowledgeId(d1.getId().longValue());
                                questionKnowledge.setKnowledgeName(d1.getName());
                                questionKnowledge.setKnowledgePath(d1.getPath());
                                questionKnowledge.setQuestionId(temp.getId());
                                questionKnowledge.setQuestionCode(temp.getCode());
                                if (user != null) {
                                    questionKnowledge.setCreateId(user.getId());
                                    questionKnowledge.setUpdateId(user.getId());
                                }
                                questionKnowledge.setCreateTime(System.currentTimeMillis());
                                questionKnowledge.setUpdateTime(System.currentTimeMillis());
                                questionKnowledgeList.add(questionKnowledge);
                            });
                        }
                        if (Collections3.isNotEmpty(d.getSubsets())) {//有子题目
                            d.getSubsets().forEach(d1 -> {
                                Question temp1 = new Question();
                                temp1.setCode(d1.getQuestionId().toString());
                                temp1.setId(d1.getQuestionId());
                                if (d1.getSubjectId() != null) {
                                    temp1.setSubjectId(d1.getSubjectId().longValue());
                                }
                                temp1.setAuditStatus(EntitySetting.COMMON_SUCCESS);
//                        if (Strings.isNullOrEmpty(d.getCreatedAt())) {
//                            Date date = DateUtil.getInstance().parseDate(d.getCreatedAt(), "yyyy-MM-dd HH:ss");
//                            temp.setCreatedAt(date.getTime());
//                        }
                                temp1.setStage(d1.getStage());
                                temp1.setContent(d1.getStem());
                                temp1.setTypeId(d1.getType().longValue());
                                temp1.setTypeName(d1.getTypeName());
                                temp1.setAuditStatus(EntitySetting.COMMON_SUCCESS);
                                if (d1.getPaperId() != null) {
                                    temp1.setPaperId(d1.getPaperId().longValue());
                                }
                                if (d1.getParentId() != null) {
                                    temp1.setSupId(d1.getPaperId().longValue());
                                }
                                temp1.setBaseType(d1.getBaseType());
                                temp1.setBaseTypeName(d1.getBaseTypeName());
                                temp1.setDifficult(d1.getDifficult());
                                temp1.setDifficultName(d1.getDifficultName());
                                temp1.setExamType(d1.getExamType());
                                temp1.setExamTypeName(d1.getExamTypeName());
                                temp1.setGrade(d1.getGrade());
                                temp1.setSource(d1.getSource());
                                if ((Collections3.isNotEmpty(d1.getOptions()))) {
                                    temp1.setOptions(JsonMapper.nonDefaultMapper().toJson(d1.getOptions()));
                                }
                                temp1.setStatus(EntitySetting.COMMON_SUCCESS);
                                if (Collections3.isEmpty(d1.getKnowledge())) {
                                    temp1.setKnowledgeNum(0);
                                } else {
                                    temp1.setKnowledgeNum(d1.getKnowledge().size());
                                    d1.getKnowledge().forEach(d2 -> {
                                        QuestionKnowledge questionKnowledge = new QuestionKnowledge();
                                        questionKnowledge.setKnowledgeId(d2.getId().longValue());
                                        questionKnowledge.setKnowledgeName(d2.getName());
                                        questionKnowledge.setKnowledgePath(d2.getPath());
                                        questionKnowledge.setQuestionId(temp1.getId());
                                        questionKnowledge.setQuestionCode(temp1.getCode());
                                        if (user != null) {
                                            questionKnowledge.setCreateId(user.getId());
                                            questionKnowledge.setUpdateId(user.getId());
                                        }
                                        questionKnowledge.setCreateTime(System.currentTimeMillis());
                                        questionKnowledge.setUpdateTime(System.currentTimeMillis());
                                        questionKnowledgeList.add(questionKnowledge);
                                    });
                                }
                                list.add(temp1);
                                questionIds.add(d1.getQuestionId());
                                QuestionExtend questionExtend = new QuestionExtend();
                                questionExtend.setUsedNum(0L);
                                questionExtend.setId(temp1.getId());
                                if (user != null) {
                                    questionExtend.setCreateId(user.getId());
                                    questionExtend.setUpdateId(user.getId());
                                }
                                questionExtend.setCreateTime(System.currentTimeMillis());
                                questionExtend.setUpdateTime(System.currentTimeMillis());
                                extendList.add(questionExtend);
                            });
                        }
                        if (user != null) {
                            temp.setCreateId(user.getId());
                            temp.setUpdateId(user.getId());
                        }
                        temp.setCreateTime(System.currentTimeMillis());
                        temp.setUpdateTime(System.currentTimeMillis());
                        list.add(temp);
                        QuestionExtend questionExtend = new QuestionExtend();
                        questionExtend.setUsedNum(0L);
                        questionExtend.setId(temp.getId());
                        if (user != null) {
                            questionExtend.setCreateId(user.getId());
                            questionExtend.setUpdateId(user.getId());
                        }
                        questionExtend.setCreateTime(System.currentTimeMillis());
                        questionExtend.setUpdateTime(System.currentTimeMillis());
                        extendList.add(questionExtend);
                        QuestionChapter questionChapter = new QuestionChapter();
                        questionChapter.setQuestionCode(temp.getCode());
                        questionChapter.setQuestionId(temp.getId());
                        questionChapter.setChapterId(chapters.getId());
                        questionChapter.setChapterName(chapters.getName());
                        questionChapter.setChapterPath(chapters.getPath());
                        if (user != null) {
                            questionChapter.setCreateId(user.getId());
                            questionChapter.setUpdateId(user.getId());
                        }
                        questionChapter.setCreateTime(System.currentTimeMillis());
                        questionChapter.setUpdateTime(System.currentTimeMillis());
                        questionChapterList.add(questionChapter);
                        codeList.add(temp.getCode());
                    }
                }
                if (Collections3.isNotEmpty(list)) {
                    dao.insertList(list);
                    //有列表数据，组装id集合查询试卷详情-获取试卷答案
                    //TODO cici begin 暂时注释掉，只同步列表
//                    if (Collections3.isNotEmpty(questionIds)) {
//                        List<QuestionAnswer> questionAnswers = Lists.newArrayList();
                    //int j=0;
//                        for (int i = 0; i < questionIds.size(); i += 100) {
//                            List<com.cnjy21.api.model.question.Question> questions = questionIds.stream().skip(j++ * 100).limit(100).map(d -> new com.cnjy21.api.model.question.Question(d)).collect(Collectors.toList());
//                            questions = service.getQuestionsAnswer(questions);
//                            if (Collections3.isNotEmpty(questions)) {
//                                questions.forEach(d -> {
//                                    QuestionAnswer questionAnswer = new QuestionAnswer();
//                                    questionAnswer.setQuestionId(d.getQuestionId());
//                                    questionAnswer.setQuestionCode(d.getQuestionId().toString());
//                                    if (Collections3.isNotEmpty(d.getOptions())) {
//                                        questionAnswer.setOptions(JsonMapper.nonDefaultMapper().toJson(d.getOptions()));
//                                    }
//                                    questionAnswer.setAnswer(d.getAnswer());
//                                    questionAnswer.setContent(d.getStem());
//                                    questionAnswer.setExplanation(d.getExplanation());
//                                    questionAnswers.add(questionAnswer);
//                                });
//                            }
//                        }
//                        if (Collections3.isNotEmpty(questionAnswers)) {
//                            questionAnswerDao.insertList(questionAnswers);
//                        }
//                    }
                    //TODO cici end 暂时注释掉，只同步列表
                }
                if (Collections3.isNotEmpty(extendList)) {
                    questionExtendDao.insertList(extendList);
                }
                if (Collections3.isNotEmpty(questionChapterList)) {
                    questionChapterDao.insertList(questionChapterList);
                }

                if (Collections3.isNotEmpty(questionKnowledgeList)) {
                    questionKnowledgeDao.insertList(questionKnowledgeList);
                }
                if (!EntitySetting.TEST_ENV && result.getPage() != null) {
                    if (result.getPage().getCurrentPage() < result.getPage().getPageCount()) {
                        codeList = syncQuestionAndAnswer(codeList, service, chapters, page + 1, perPage);
                    }
                }
            }
        }
        return codeList;
    }

    public PageDto<QuestionSearchRspDto> searchQuestion(QuestionSearchReqDto reqDto) {
        PageDto<QuestionSearchRspDto> dto = new PageDto<>(true);
        if (reqDto == null) {
            dto.setMsg("参数为空");
        } else {
            Integer count = dao.selectCountBySearch(reqDto);
            dto.setCount(count);
            dto.setPage(reqDto.getP());
            dto.setSize(reqDto.getS());
            if (reqDto.getS() != null) {
                dto.setCountPage(count % reqDto.getS() == 0 ? count / reqDto.getS() : count / reqDto.getS() + 1);
            }
            if (count != null && count != 0) {
                List<QuestionSearchRspDto> list = dao.selectBySearch(reqDto);
                //如果传输用户,判断是否有收藏
                if (Collections3.isNotEmpty(list) && reqDto.getUserId() != null && reqDto.getUserId() != 0L) {
                    List<Long> questionIds = list.stream().map(d -> d.getId()).collect(Collectors.toList());
                    List<UserCollection> userCollections = userCollectionDao.selectByExample(new Example.Builder(UserCollection.class)
                            .where(WeekendSqls.<UserCollection>custom().andIn(UserCollection::getDataId, questionIds)
                                    .andEqualTo(UserCollection::getTypeId, EntitySetting.DATA_TYPE_QUESTION)
                                    .andEqualTo(UserCollection::getUserId, reqDto.getUserId()))
                            .build());
                    questionIds = userCollections.stream().map(d -> d.getDataId()).collect(Collectors.toList());
                    List<QuestionSearchRspDto> tempList = Lists.newArrayList();
                    for (QuestionSearchRspDto d : list) {
                        if (questionIds.contains(d.getId())) {
                            d.setCollection(true);
                        }
                        tempList.add(d);
                    }
                    dto.setList(tempList);
                } else {
                    dto.setList(list);
                }
            }
        }
        return dto;
    }

    public DetailDto<PapersQuestionDto> makePapersQuestion(List<Long> questionIds) {
        DetailDto<PapersQuestionDto> dto = new DetailDto<>(false);
        if (Collections3.isEmpty(questionIds)) {
            dto.setMsg("参数为空");
        } else {
            dto.setStatus(true);
            //查询所有的试题信息
            List<Question> questionList = dao.selectByExample(new Example.Builder(Question.class)
                    .where(WeekendSqls.<Question>custom().andIn(Question::getId, questionIds))
                    .build());
            PapersQuestionDto detail = new PapersQuestionDto();
            detail.setTitle(DateUtil.getInstance().formatDate(System.currentTimeMillis(), "yyyy年MM月dd日"));
            dto = papersQuestionDetail(dto, detail, questionList);
        }
        return dto;
    }


    public DetailDto<PapersQuestionDto> makeTemplatePapersQuestionByChapter(ChapterToPaperDto reqDto) {
        DetailDto<PapersQuestionDto> dto = new DetailDto<>(false);
        if (reqDto == null) {
            dto.setMsg("参数为空");
        } else if (reqDto.getStyle() == null) {
            dto.setMsg("出题方式为空");
        } else if (Collections3.isEmpty(reqDto.getSelectedIds())) {
            dto.setMsg("章节集合为空");
        } else if (Collections3.isEmpty(reqDto.getTypes())) {
            dto.setMsg("题型为空");
        } else {
            dto.setStatus(true);
            PapersQuestionDto detail = new PapersQuestionDto();
            detail.setTitle(DateUtil.getInstance().formatDate(System.currentTimeMillis(), "yyyy年MM月dd日"));
            List<Question> questionList = Lists.newArrayList();
            if (2 == reqDto.getStyle()) {//精准出题
                //select * from question where type_id=? and id in(select chapter_id from question_chapter where )
                reqDto.getTypes().forEach(d -> {
                    if (d.getNum() != null && d.getNum() != 0) {
                        if (Collections3.isEmpty(d.getContactIds())) {
                            questionList.addAll(dao.selectByTypeIdAndChapterAndNum(d.getId(), reqDto.getSelectedIds(), d.getDifficult(), d.getNum()));
                        } else {
                            questionList.addAll(dao.selectByTypeIdAndChapterAndNum(d.getId(), d.getContactIds(), d.getDifficult(), d.getNum()));
                        }
                    }
                });
            } else {//关联出题
                reqDto.getTypes().forEach(d -> {
                    if (d.getNum() != null && d.getNum() != 0) {
                        //先查询一条记录是精准的,其他是任意的
                        if (Collections3.isEmpty(d.getContactIds())) {
                            questionList.addAll(dao.selectByTypeIdAndChapterAndNum(d.getId(), reqDto.getSelectedIds(), d.getDifficult(), d.getNum()));
                        } else {
                            questionList.addAll(dao.selectByTypeIdAndChapterAndNum(d.getId(), d.getContactIds(), d.getDifficult(), 1));
                            if (d.getNum() > 1) {
                                questionList.addAll(dao.selectByTypeIdAndChapterAndNum(d.getId(), reqDto.getSelectedIds(), d.getDifficult(), d.getNum() - 1));
                            }
                        }
                    }
                });
            }
            dto = papersQuestionDetail(dto, detail, questionList);
        }
        return dto;
    }

    public DetailDto<PapersQuestionDto> makeTemplatePapersQuestionByKnowledge(ChapterToPaperDto reqDto) {
        DetailDto<PapersQuestionDto> dto = new DetailDto<>(false);
        if (reqDto == null) {
            dto.setMsg("参数为空");
        } else if (reqDto.getStyle() == null) {
            dto.setMsg("出题方式为空");
        } else if (Collections3.isEmpty(reqDto.getSelectedIds())) {
            dto.setMsg("知识点集合为空");
        } else if (Collections3.isEmpty(reqDto.getTypes())) {
            dto.setMsg("题型为空");
        } else {
            dto.setStatus(true);
            PapersQuestionDto detail = new PapersQuestionDto();
            detail.setTitle(DateUtil.getInstance().formatDate(System.currentTimeMillis(), "yyyy年MM月dd日"));
            List<Question> questionList = Lists.newArrayList();
            if (2 == reqDto.getStyle()) {//精准出题
                //select * from question where type_id=? and id in(select chapter_id from question_chapter where )
                reqDto.getTypes().forEach(d -> {
                    if (d.getNum() != null && d.getNum() != 0) {
                        if (Collections3.isEmpty(d.getContactIds())) {
                            questionList.addAll(dao.selectByTypeIdAndKnowledgeAndNum(d.getId(), reqDto.getSelectedIds(), d.getDifficult(), d.getNum()));
                        } else {
                            questionList.addAll(dao.selectByTypeIdAndKnowledgeAndNum(d.getId(), d.getContactIds(), d.getDifficult(), d.getNum()));
                        }
                    }
                });
            } else {//关联出题
                reqDto.getTypes().forEach(d -> {
                    if (d.getNum() != null && d.getNum() != 0) {
                        //先查询一条记录是精准的,其他是任意的
                        if (Collections3.isEmpty(d.getContactIds())) {
                            questionList.addAll(dao.selectByTypeIdAndKnowledgeAndNum(d.getId(), reqDto.getSelectedIds(), d.getDifficult(), d.getNum()));
                        } else {
                            questionList.addAll(dao.selectByTypeIdAndKnowledgeAndNum(d.getId(), d.getContactIds(), d.getDifficult(), 1));
                            if (d.getNum() > 1) {
                                questionList.addAll(dao.selectByTypeIdAndKnowledgeAndNum(d.getId(), reqDto.getSelectedIds(), d.getDifficult(), d.getNum() - 1));
                            }
                        }
                    }
                });
            }
            dto = papersQuestionDetail(dto, detail, questionList);
        }
        return dto;
    }

    public DetailDto<PapersQuestionDto> makePapersByPaperId(Long paperId) {
        DetailDto<PapersQuestionDto> dto = new DetailDto<>(false);
        if (paperId == null || paperId == 0L) {
            dto.setMsg("参数为空");
        } else {
            Papers papers = papersDao.selectByPrimaryKey(paperId);
            if (papers == null) {
                dto.setMsg("试卷不存在");
            } else {
                dto.setStatus(true);
                PapersQuestionDto detail = new PapersQuestionDto();
                detail.setTitle(papers.getTitle());
                List<Question> questionList = dao.selectByExample(new Example.Builder(Question.class)
                        .where(WeekendSqls.<Question>custom()
                                .andEqualTo(Question::getPaperId, paperId)).build());
                dto = papersQuestionDetail(dto, detail, questionList);
                detail.setPaperId(paperId);
            }
        }
        return dto;
    }

    private DetailDto<PapersQuestionDto> papersQuestionDetail(DetailDto<PapersQuestionDto> dto, PapersQuestionDto detail, List<Question> questionList) {
        List<PapersQuestionTypeDto> typeList = Lists.newArrayList();//类型集合
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
            //查询试题关联知识点
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
        return dto;
    }

    public ListDto<QuestionChangeDto> changeQuestionById(Long questionId, Integer p, Integer s) {
        ListDto<QuestionChangeDto> dto = new ListDto<>(false);
        if (questionId == null || questionId == 0L) {
            dto.setMsg("数据id为空");
        } else {
            Question question = dao.selectByPrimaryKey(questionId);
            if (question == null) {
                dto.setMsg("数据不存在");
            } else {
                if (p == null) {
                    p = 0;
                }
                if (s == null) {
                    s = 1;
                }
                Question temp = new Question();
                temp.setStatus(EntitySetting.COMMON_SUCCESS);
                temp.setGrade(question.getGrade());
                temp.setAuditStatus(EntitySetting.COMMON_SUCCESS);
                temp.setBaseType(question.getBaseType());
                temp.setStage(question.getStage());
                temp.setSubjectId(question.getSubjectId());
                List<Question> list = dao.selectByRowBounds(temp, new RowBounds(p * s, s));
                //根据试题查询试题的答案
                List<Long> questionIds = list.stream().map(d -> d.getId()).collect(Collectors.toList());
                List<QuestionAnswer> answers = questionAnswerDao.selectByExample(new Example.Builder(QuestionAnswer.class)
                        .where(WeekendSqls.<QuestionAnswer>custom().andIn(QuestionAnswer::getQuestionId, questionIds)).build());
                Map<Long, QuestionAnswer> answerMap = Maps.newHashMap();
                answers.forEach(d -> {
                    answerMap.put(d.getQuestionId(), d);
                });
                List<QuestionChangeDto> dtoList = Lists.newArrayList();
                list.forEach(d -> {
                    QuestionChangeDto q = BeanMapper.map(d, QuestionChangeDto.class);
                    if (answerMap.containsKey(d.getId())) {
                        QuestionAnswer questionAnswer = answerMap.get(d.getId());
                        q.setAnswer(questionAnswer.getAnswer());
                        q.setExplanation(questionAnswer.getExplanation());
                    }
                    dtoList.add(q);
                });
                dto.setList(dtoList);
                dto.setStatus(true);
            }

        }
        return dto;
    }

    public DetailDto<QuestionTypeAndChapterDto> findTypeAndCountQuestion(List<Long> ids) {
        DetailDto<QuestionTypeAndChapterDto> dto = new DetailDto<>(false);
        if (Collections3.isEmpty(ids)) {
            dto.setMsg("id集合为空");
        } else {
            QuestionTypeAndChapterDto detail = new QuestionTypeAndChapterDto();
            //章节包含的题型&题目总和
            List<QuestionTypeDto> typeList = dao.selectCountByChapterIdsGroupByType(ids, false);
            //其他
            List<QuestionTypeDto> otherList = dao.selectCountByChapterIdsGroupByType(ids, true);
            detail.setTypes(typeList);
            detail.setOtherTypes(otherList);
            Long num = 0L;
            for (QuestionTypeDto d : otherList) {
                num += d.getNum();
            }
            detail.setOtherNum(num);
            Long typeNum = 0L;
            for (QuestionTypeDto d : typeList) {
                typeNum += d.getNum();
            }
            detail.setQuestionSum(num + typeNum);
            dto.setDetail(detail);
            dto.setStatus(true);
        }
        return dto;
    }


    public ListDto<CommonIdAndNameDto> findQuestionKnowledge(PaperTemplateReqDto reqDto) {
        ListDto<CommonIdAndNameDto> dto = new ListDto<>(false);
        if (reqDto == null) {
            dto.setMsg("参数为空");
        } else {
            List<QuestionKnowledge> knowledges = questionKnowledgeDao.selectByQuestionType(reqDto);
            if (Collections3.isNotEmpty(knowledges)) {
                List<CommonIdAndNameDto> list = Lists.newArrayList();
                Map<Long, String> map = Maps.newHashMap();
                knowledges.forEach(d -> {
                    map.put(d.getKnowledgeId(), d.getKnowledgeName());
                });
                map.forEach((k, v) -> {
                    CommonIdAndNameDto temp = new CommonIdAndNameDto();
                    temp.setId(k);
                    temp.setName(v);
                    list.add(temp);
                });
                dto.setList(list);
            }
            dto.setStatus(true);
        }
        return dto;
    }

}