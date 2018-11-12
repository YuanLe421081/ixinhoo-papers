package com.ixinhoo.papers.service.papers;

import com.chunecai.crumbs.api.client.DetailDto;
import com.chunecai.crumbs.api.client.ListDto;
import com.chunecai.crumbs.api.client.PageDto;
import com.chunecai.crumbs.api.client.StatusDto;
import com.chunecai.crumbs.api.dao.BaseDao;
import com.chunecai.crumbs.api.service.BaseService;
import com.chunecai.crumbs.api.shiro.ShiroUser;
import com.chunecai.crumbs.api.util.UsersUtil;
import com.chunecai.crumbs.code.util.collection.Collections3;
import com.chunecai.crumbs.code.util.date.DateUtil;
import com.chunecai.crumbs.code.util.mapper.BeanMapper;
import com.chunecai.crumbs.code.util.mapper.JsonMapper;
import com.cnjy21.api.service.APIPaperService;
import com.cnjy21.api.service.APIRequestParams;
import com.cnjy21.api.util.APIResult;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ixinhoo.papers.dao.common.InterfaceInfoDao;
import com.ixinhoo.papers.dao.papers.*;
import com.ixinhoo.papers.dao.website.CoinSettingDao;
import com.ixinhoo.papers.dto.common.CommonIdAndNameDto;
import com.ixinhoo.papers.dto.common.CommonIdNameAndNumDto;
import com.ixinhoo.papers.dto.papers.*;
import com.ixinhoo.papers.dto.user.paper.UserPaperTemplateSaveDto;
import com.ixinhoo.papers.dto.user.paper.UserPaperTemplateSaveTypeDto;
import com.ixinhoo.papers.dto.user.paper.UserPaperTemplateSaveTypeKnowledgeDto;
import com.ixinhoo.papers.entity.EntitySetting;
import com.ixinhoo.papers.entity.common.InterfaceInfo;
import com.ixinhoo.papers.entity.papers.*;
import com.ixinhoo.papers.entity.user.UserPaperTemplate;
import com.ixinhoo.papers.entity.user.UserPaperTemplateQuestion;
import com.ixinhoo.papers.entity.user.UserPaperTemplateType;
import com.ixinhoo.papers.entity.website.CoinSetting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.weekend.WeekendSqls;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PapersService extends BaseService<Papers> {
    @Autowired
    private PapersDao dao;
    @Autowired
    private InterfaceInfoDao interfaceInfoDao;
    @Autowired
    private PaperChapterDao paperChapterDao;
    @Autowired
    private PaperProvinceDao paperProvinceDao;
    @Autowired
    private QuestionDao questionDao;
    @Autowired
    private QuestionKnowledgeDao questionKnowledgeDao;
    @Autowired
    private CoinSettingDao coinSettingDao;

    @Override
    protected BaseDao getBaseDao() {
        return dao;
    }

    @Transactional
    public StatusDto syncPapers() {
        StatusDto dto = new StatusDto(true);
        APIPaperService service = new APIPaperService();
        //需要查询原来的数据进行增量更新，
        List<Papers> dbPapers = dao.selectAll();
        List<String> codeList = dbPapers.stream().map(d -> d.getCode()).collect(Collectors.toList());
        syncPapersAndPaperChapter(codeList, service, EntitySetting.INTERFACE_FIRST_PAGE, EntitySetting.INTERFACE_MAX_PERPAGE);
        return dto;
    }

    @Transactional
    private void syncPapersAndPaperChapter(List<String> codeList, APIPaperService service, int page, int perPage) {
        APIRequestParams apiRequestParams = new APIRequestParams();
        apiRequestParams.page = page;
        apiRequestParams.perPage = perPage;
        APIResult<com.cnjy21.api.model.papers.Papers> result = service.getPapers(apiRequestParams);
        if (result != null && Collections3.isNotEmpty(result.getData())) {
            InterfaceInfo interfaceInfo = new InterfaceInfo();
            interfaceInfo.setTime(System.currentTimeMillis());
            interfaceInfo.setType(EntitySetting.INTERFACE_PAPERS);
            interfaceInfo.setReqData(JsonMapper.nonDefaultMapper().toJson(apiRequestParams));
            interfaceInfo.setRspData(JsonMapper.nonDefaultMapper().toJson(result));
            ShiroUser user = UsersUtil.ShiroUser();
            if (user != null) {
                interfaceInfo.setCreateId(user.getId());
                interfaceInfo.setUpdateId(user.getId());
            }
            interfaceInfo.setCreateTime(System.currentTimeMillis());
            interfaceInfo.setUpdateTime(System.currentTimeMillis());
            interfaceInfoDao.insert(interfaceInfo);
            //判断页数与返回的数据是否一致循环获取试卷列表
            List<Papers> list = Lists.newArrayList();
            List<PaperChapter> paperChapterList = Lists.newArrayList();
            List<PaperProvince> paperProvinceList = Lists.newArrayList();
            result.getData().forEach(d -> {
                if (!codeList.contains(d.getId().toString())) {
                    //TODO cici
                    Papers temp = new Papers();
                    temp.setCode(d.getId().toString());
                    temp.setId(d.getId().longValue());
                    if (d.getSubjectId() != null) {
                        temp.setSubjectId(d.getSubjectId().longValue());
                    }
                    temp.setAuditStatus(EntitySetting.COMMON_SUCCESS);
                    if (d.getBookId() != null) {
                        temp.setBookId(d.getBookId().longValue());
                    }
                    if (d.getVersionId() != null) {
                        temp.setBookId(d.getVersionId().longValue());
                    }
                    if (!Strings.isNullOrEmpty(d.getUpdatedAt())) {
                        Date date = DateUtil.getInstance().parseDate(d.getUpdatedAt(), "yyyy-MM-dd HH:mm:ss");
                        temp.setUpdatedAt(date.getTime());
                    }
                    if (!Strings.isNullOrEmpty(d.getCreatedAt())) {
                        Date date = DateUtil.getInstance().parseDate(d.getCreatedAt(), "yyyy-MM-dd HH:mm:ss");
                        temp.setCreatedAt(date.getTime());
                    }else{
                        temp.setCreatedAt(System.currentTimeMillis());
                    }
                    if(temp.getUpdatedAt()==null){
                        temp.setUpdatedAt(temp.getCreatedAt());
                    }
                    temp.setShareStatus(EntitySetting.PAPER_UESR_SHARE_AUDIT_PASS);
                    temp.setStage(d.getStage());
                    temp.setTitle(d.getTitle());
                    temp.setTypeId(d.getType().longValue());
                    temp.setTypeName(d.getTypeText());
                    temp.setAuditStatus(EntitySetting.COMMON_SUCCESS);
                    temp.setYear(d.getYear());
                    temp.setVersionId(d.getVersionId().longValue());
                    if (Collections3.isNotEmpty(d.getCategories())) {//章节不为空
                        d.getCategories().forEach(d1 -> {
                            if (!Strings.isNullOrEmpty(d1) && d1.matches("[\\d]+")) {
                                PaperChapter paperChapter = new PaperChapter();
                                paperChapter.setChapterId(Long.parseLong(d1));
                                paperChapter.setPaperId(temp.getId());
                                paperChapter.setPaperCode(temp.getCode());
                                if (user != null) {
                                    paperChapter.setCreateId(user.getId());
                                    paperChapter.setUpdateId(user.getId());
                                }
                                paperChapter.setCreateTime(System.currentTimeMillis());
                                paperChapter.setUpdateTime(System.currentTimeMillis());
                                paperChapterList.add(paperChapter);
                            }
                        });

                    }
                    if (Collections3.isNotEmpty(d.getProvinces())) {
                        d.getProvinces().forEach(d1 -> {
                            PaperProvince paperProvince = new PaperProvince();
                            paperProvince.setPaperId(temp.getId());
                            paperProvince.setPaperCode(temp.getCode());
                            paperProvince.setProvinceId(d1.getId().longValue());
                            paperProvince.setProvinceName(d1.getName());
                            if (user != null) {
                                paperProvince.setCreateId(user.getId());
                                paperProvince.setUpdateId(user.getId());
                            }
                            paperProvince.setCreateTime(System.currentTimeMillis());
                            paperProvince.setUpdateTime(System.currentTimeMillis());
                            paperProvinceList.add(paperProvince);
                        });

                    }
                    if (user != null) {
                        temp.setCreateId(user.getId());
                        temp.setUpdateId(user.getId());
                    }
                    temp.setCreateTime(System.currentTimeMillis());
                    temp.setUpdateTime(System.currentTimeMillis());
                    list.add(temp);
                }
            });
            if (Collections3.isNotEmpty(list)) {
                dao.insertList(list);
            }
            if (Collections3.isNotEmpty(paperChapterList)) {
                paperChapterDao.insertList(paperChapterList);
            }
            if (Collections3.isNotEmpty(paperProvinceList)) {
                paperProvinceDao.insertList(paperProvinceList);
            }
            if (!EntitySetting.TEST_ENV && result.getPage() != null) {
                if (result.getPage().getCurrentPage() < result.getPage().getPageCount()) {
                    syncPapersAndPaperChapter(codeList, service, page + 1, perPage);
                }
            }
        }
    }

    /**
     * 先按照试卷的使用次数进行降序推荐
     *
     * @return
     */
    public ListDto<PapersRecommendDto> listPapersRecommend() {
        ListDto<PapersRecommendDto> dto = new ListDto<>(true);
        List<Papers> list = dao.selectPapersRecommendByPage(0, 6);
        dto.setList(BeanMapper.mapList(list, PapersRecommendDto.class));
        return dto;
    }

    public ListDto<CommonIdAndNameDto> findPaperTypeInTemplate(PaperTemplateReqDto reqDto) {
        ListDto<CommonIdAndNameDto> dto = new ListDto<>(true);
        if (reqDto != null) {
            List<Papers> papers = dao.selectDistinctByTemplate(reqDto);
            List<CommonIdAndNameDto> list = Lists.newArrayList();
            if (Collections3.isNotEmpty(papers)) {
                papers.forEach(d -> {
                    CommonIdAndNameDto temp = new CommonIdAndNameDto();
                    temp.setId(d.getTypeId());
                    temp.setName(d.getTypeName());
                    list.add(temp);
                });
            }
            if (reqDto.getUserId() != null && reqDto.getUserId() != 0L) {
                CommonIdAndNameDto temp = new CommonIdAndNameDto();
                temp.setId(-1L);
                temp.setName("我的模板");
                list.add(temp);
            }
            dto.setList(list);
        }
        return dto;
    }

    public PageDto<PaperTemplateRspDto> findPaperByTemplate(PaperTemplateReqDto reqDto) {
        PageDto<PaperTemplateRspDto> dto = new PageDto<>(true);
        if (reqDto != null) {
            List<PaperTemplateRspDto> papers = dao.selectPaperByTemplate(reqDto);
            int count = dao.selectCountPaperByTemplate(reqDto);
            dto.setPage(reqDto.getP());
            dto.setCountPage(count / reqDto.getS());
            dto.setCount(count);
            dto.setSize(reqDto.getS());
            dto.setList(papers);
        }
        return dto;
    }

    public DetailDto<PaperAnalysisDto> paperAnalysis(Long id) {
        DetailDto<PaperAnalysisDto> dto = new DetailDto<>(false);
        if (id == null || id == 0L) {
            dto.setMsg("id为空");
        } else {
            Papers papers = dao.selectByPrimaryKey(id);
            if (papers == null) {
                dto.setMsg("试卷不存在");
            } else {
                List<Question> questionList = questionDao.selectByExample(
                        new Example.Builder(Question.class)
                                .where(WeekendSqls.<Question>custom()
                                        .andEqualTo(Question::getPaperId, id)).build());
                if (Collections3.isEmpty(questionList)) {
                    dto.setMsg("试卷下的试题为空");
                } else {
                    //组装数据
                    Map<Integer, List<Question>> difficultMap = Maps.newHashMap();
                    Map<Long, List<Question>> typeMap = Maps.newHashMap();
                    List<Long> questionIds = Lists.newArrayList();
                    questionList.forEach(d -> {
                        questionIds.add(d.getId());
                        List<Question> list1;
                        if (difficultMap.containsKey(d.getDifficult())) {
                            list1 = difficultMap.get(d.getDifficult());
                        } else {
                            list1 = Lists.newArrayList();
                        }
                        list1.add(d);
                        difficultMap.put(d.getDifficult(), list1);
                        if (typeMap.containsKey(d.getDifficult())) {
                            list1 = typeMap.get(d.getTypeId());
                        } else {
                            list1 = Lists.newArrayList();
                        }
                        list1.add(d);
                        typeMap.put(d.getTypeId(), list1);
                    });
                    List<CommonIdNameAndNumDto> diffcultList = Lists.newArrayList();
                    difficultMap.forEach((k, v) -> {
                        CommonIdNameAndNumDto temp = new CommonIdNameAndNumDto();
                        temp.setId(k.longValue());
                        temp.setName(v.get(0).getDifficultName());
                        temp.setNum(v.size() + 0L);
                        diffcultList.add(temp);
                    });
                    List<CommonIdNameAndNumDto> typeList = Lists.newArrayList();
                    typeMap.forEach((k, v) -> {
                        CommonIdNameAndNumDto temp = new CommonIdNameAndNumDto();
                        temp.setId(k);
                        temp.setName(v.get(0).getTypeName());
                        temp.setNum(v.size() + 0L);
                        typeList.add(temp);
                    });
                    //查询知识点
                    List<QuestionKnowledge> questionKnowledgeList = questionKnowledgeDao.selectByExample(new Example.Builder(QuestionKnowledge.class)
                            .where(WeekendSqls.<QuestionKnowledge>custom()
                                    .andIn(QuestionKnowledge::getQuestionId, questionIds)).build());
                    Map<Long, List<QuestionKnowledge>> knowledgeMap = Maps.newHashMap();
                    questionKnowledgeList.forEach(d -> {
                        List<QuestionKnowledge> list1;
                        if (knowledgeMap.containsKey(d.getKnowledgeId())) {
                            list1 = knowledgeMap.get(d.getKnowledgeId());
                        } else {
                            list1 = Lists.newArrayList();
                        }
                        list1.add(d);
                        knowledgeMap.put(d.getKnowledgeId(), list1);
                    });
                    List<CommonIdNameAndNumDto> knowledgeList = Lists.newArrayList();
                    knowledgeMap.forEach((k, v) -> {
                        CommonIdNameAndNumDto temp = new CommonIdNameAndNumDto();
                        temp.setId(k.longValue());
                        temp.setName(v.get(0).getKnowledgeName());
                        temp.setNum(v.size() + 0L);
                        knowledgeList.add(temp);
                    });
                    dto.setStatus(true);
                    PaperAnalysisDto detail = new PaperAnalysisDto();
                    detail.setDifficult(diffcultList);
                    detail.setKnowledge(knowledgeList);
                    detail.setType(typeList);
                    detail.setId(papers.getId());
                    detail.setTitle(papers.getTitle());
                    dto.setDetail(detail);
                }
            }
        }
        return dto;
    }

    public PageDto<PapersSearchRspDto> searchByChapter(PapersSearchByChapterReqDto reqDto) {
        PageDto<PapersSearchRspDto> dto = new PageDto<>(true);
        if (reqDto != null) {
            List<PapersSearchRspDto> papers = dao.selectBySearchChapter(reqDto);
            if(Collections3.isNotEmpty(papers)){//查询试卷所需的备课币
                CoinSetting coinSetting = coinSettingDao.selectAll().get(0);
                papers.forEach(d->{
                    d.setCoin(coinSetting.getPaperCoin());
                });
            }
            int count = dao.selectCountBySearchChapter(reqDto);
            dto.setPage(reqDto.getP());
            dto.setCountPage(count / reqDto.getS());
            dto.setCount(count);
            dto.setSize(reqDto.getS());
            dto.setList(papers);
        }
        return dto;
    }

    public PageDto<PapersSearchRspDto> searchByGrade(PapersSearchByGradeReqDto reqDto) {
        PageDto<PapersSearchRspDto> dto = new PageDto<>(true);
        if (reqDto != null) {
            List<PapersSearchRspDto> papers = dao.selectBySearchGrade(reqDto);
            if(Collections3.isNotEmpty(papers)){//查询试卷所需的备课币
                CoinSetting coinSetting = coinSettingDao.selectAll().get(0);
                papers.forEach(d->{
                    d.setCoin(coinSetting.getPaperCoin());
                });
            }
            int count = dao.selectCountBySearchGrade(reqDto);
            dto.setPage(reqDto.getP());
            dto.setCountPage(count / reqDto.getS());
            dto.setCount(count);
            dto.setSize(reqDto.getS());
            dto.setList(papers);
        }
        return dto;
    }

    public DetailDto<UserPaperTemplateSaveDto> findPaperTemplateById(Long id) {
        DetailDto<UserPaperTemplateSaveDto> dto = new DetailDto<>(false);
        if (id == null || id == 0L) {
            dto.setMsg("id为空");
        } else {
            Papers papers = super.findById(id);
            if (papers == null) {
                dto.setMsg("数据不存在");
            } else {
                dto.setStatus(true);
                /**
                 * private Long userId;//用户id
                 private Long id;//模板id
                 private String title;//标题
                 private Integer stage;//学段
                 private Long subjectId;//学科id
                 private String subjectName;//学科名称
                 private Integer grade;//年级
                 private Integer term;//学期
                 private Long versionId;//版本id
                 private List<UserPaperTemplateSaveTypeDto> types;//题型
                 */
                UserPaperTemplateSaveDto detail = new UserPaperTemplateSaveDto();
                detail.setUserId(null);
                detail.setTitle(papers.getTitle());
                detail.setStage(papers.getStage());
                detail.setGrade(null);
                detail.setId(papers.getId());
                detail.setSubjectId(papers.getSubjectId());
                detail.setSubjectName(null);
                detail.setTerm(null);
                detail.setVersionId(papers.getVersionId());
                //查询分类
//                if (Collections3.isNotEmpty(typeList)) {
//                    List<UserPaperTemplateSaveTypeDto> types = Lists.newArrayList();
//                    typeList.sort((o1, o2) -> o1.getSort() - o2.getSort());
//                    typeList.forEach(d -> {
//                        UserPaperTemplateSaveTypeDto temp = new UserPaperTemplateSaveTypeDto();
//                        temp.setId(d.getTypeId());
//                        temp.setName(d.getTypeName());
//                        List<UserPaperTemplateQuestion> questionList = templateQuestionDao.selectByExample(new Example.Builder(UserPaperTemplateQuestion.class)
//                                .where(WeekendSqls.<UserPaperTemplateQuestion>custom()
//                                        .andEqualTo(UserPaperTemplateQuestion::getTypeId, d.getId())
//                                        .andEqualTo(UserPaperTemplateQuestion::getTemplateId, papers.getId())).build());
//                        if (Collections3.isNotEmpty(questionList)) {
//                            List<UserPaperTemplateSaveTypeKnowledgeDto> questions = Lists.newArrayList();
//                            questionList.sort((o1, o2) -> o1.getSort() - o2.getSort());
//                            questionList.forEach(d1 -> {
//                                UserPaperTemplateSaveTypeKnowledgeDto temp1 = new UserPaperTemplateSaveTypeKnowledgeDto();
//                                temp1.setDifficult(d1.getDifficult());
//                                if (!Strings.isNullOrEmpty(d1.getKnowledgeIds())) {
//                                    List<CommonIdAndNameDto> list1 = Lists.newArrayList();
//                                    String[] k1 = d1.getKnowledgeIds().split(",");
//                                    String[] k2 = d1.getKnowledgeNames().split(",");
//                                    for (int i = 0; i < k1.length; i++) {
//                                        CommonIdAndNameDto c = new CommonIdAndNameDto();
//                                        c.setId(Long.parseLong(k1[i]));
//                                        c.setName(k2[i]);
//                                        list1.add(c);
//                                    }
//                                    temp1.setKnowledge(list1);
//                                }
//                                questions.add(temp1);
//                            });
//                            temp.setQuestions(questions);
//                        }
//                        types.add(temp);
//                    });
//                    detail.setTypes(types);
//                }
                dto.setDetail(detail);
            }
        }
        return dto;
    }
}