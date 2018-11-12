package com.ixinhoo.papers.service.user;

import com.chunecai.crumbs.api.client.ListDto;
import com.chunecai.crumbs.api.client.PageDto;
import com.chunecai.crumbs.api.client.StatusDto;
import com.chunecai.crumbs.api.dao.BaseDao;
import com.chunecai.crumbs.api.service.BaseService;
import com.chunecai.crumbs.code.util.collection.Collections3;
import com.chunecai.crumbs.code.util.mapper.BeanMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ixinhoo.papers.dao.base.SubjectsDao;
import com.ixinhoo.papers.dao.papers.QuestionDao;
import com.ixinhoo.papers.dao.resources.DocumentDao;
import com.ixinhoo.papers.dao.user.UserCollectionDao;
import com.ixinhoo.papers.dao.user.UserDao;
import com.ixinhoo.papers.dto.base.StageSubjectDto;
import com.ixinhoo.papers.dto.common.CommonIdAndNameDto;
import com.ixinhoo.papers.dto.user.collection.UserCollectionQuestionReqDto;
import com.ixinhoo.papers.dto.user.collection.UserCollectionQuestionRspDto;
import com.ixinhoo.papers.dto.user.collection.UserCollectionReqDto;
import com.ixinhoo.papers.dto.user.collection.UserCollectionRspDto;
import com.ixinhoo.papers.entity.EntitySetting;
import com.ixinhoo.papers.entity.base.Subjects;
import com.ixinhoo.papers.entity.papers.Question;
import com.ixinhoo.papers.entity.resources.Document;
import com.ixinhoo.papers.entity.user.User;
import com.ixinhoo.papers.entity.user.UserCollection;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.weekend.WeekendSqls;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserCollectionService extends BaseService<UserCollection> {
    @Autowired
    private UserCollectionDao dao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private DocumentDao documentDao;
    @Autowired
    private QuestionDao questionDao;
    @Autowired
    private SubjectsDao subjectsDao;


    @Override
    protected BaseDao getBaseDao() {
        return dao;
    }

    @Transactional
    public StatusDto createByUserAndDocument(Long userId, List<Long> documentIds) {
        StatusDto dto = new StatusDto(false);
        if (userId == null || userId == 0L) {
            dto.setMsg("用户未传输");
        } else if (Collections3.isEmpty(documentIds)) {
            dto.setMsg("文档未传输");
        } else {
            User user = userDao.selectByPrimaryKey(userId);
            if (user == null) {
                dto.setMsg("用户不存在");
            } else {
                List<Document> documents = documentDao.selectByExample(new Example.Builder(Document.class)
                        .where(WeekendSqls.<Document>custom().andEqualTo(Document::getStatus, EntitySetting.COMMON_SUCCESS)
                                .andIn(Document::getId, documentIds))
                        .build());
                documents.forEach(d -> {
                    UserCollection userCollection = dao.selectOneByExample(new Example.Builder(UserCollection.class)
                            .where(WeekendSqls.<UserCollection>custom().andEqualTo(UserCollection::getUserId, userId)
                                    .andEqualTo(UserCollection::getTypeId, d.getTypeId())
                                    .andEqualTo(UserCollection::getDataId, d.getId()))
                            .build());
                    if (userCollection == null) {
                        userCollection = new UserCollection();
                        userCollection.setDataId(d.getId());
                        userCollection.setTypeId(d.getTypeId());
                        userCollection.setUserId(userId);
                        userCollection.setTime(System.currentTimeMillis());
                        super.create(userCollection);
                        dto.setStatus(true);
                    }else{
                        dto.setMsg("您已收藏过了");
                    }
                });
            }
        }
        return dto;
    }

    @Transactional
    public StatusDto createByUserAndQuestion(Long userId, List<Long> questionIds) {
        StatusDto dto = new StatusDto(false);
        if (userId == null || userId == 0L) {
            dto.setMsg("用户未传输");
        } else if (Collections3.isEmpty(questionIds)) {
            dto.setMsg("题目未传输");
        } else {
            User user = userDao.selectByPrimaryKey(userId);
            if (user == null) {
                dto.setMsg("用户不存在");
            } else {
                questionIds.forEach(d -> {
                    UserCollection userCollection = new UserCollection();
                    userCollection.setDataId(d);
                    userCollection.setUserId(userId);
                    userCollection = dao.selectOne(userCollection);
                    if (userCollection == null) {
                        userCollection = new UserCollection();
                        userCollection.setDataId(d);
                        userCollection.setTypeId(EntitySetting.DATA_TYPE_QUESTION);
                        userCollection.setUserId(userId);
                        userCollection.setTime(System.currentTimeMillis());
                        super.create(userCollection);
                        dto.setStatus(true);
                    }else{
                        dto.setMsg("您已收藏过了");
                    }
                });
            }
        }
        return dto;
    }

    public PageDto<UserCollectionRspDto> listDocumentByUserCollection(UserCollectionReqDto reqDto) {
        PageDto<UserCollectionRspDto> dto = new PageDto<>(false);
        if (reqDto == null) {
            dto.setMsg("参数为空");
        } else if (reqDto.getUserId() == null || reqDto.getUserId() == 0L) {
            dto.setMsg("用户为空");
        } else {
            dto.setStatus(true);
            if (reqDto.getP() == null) {
                reqDto.setP(0);
            }
            if (reqDto.getS() == null) {
                reqDto.setS(10);
            }
            //（3 = '课件', 8 = '教案', 7 = '试卷', 4 = '学案',12 = '视频'）20-试题;0--其他
            Integer type = reqDto.getType();
            List<UserCollection> collections;
            if (type == null || type == 0L) {
                List<Integer> typeIds = Lists.newArrayList();
                typeIds.add(3);
                typeIds.add(8);
                typeIds.add(7);
                typeIds.add(4);
                typeIds.add(12);
                typeIds.add(20);
                collections = dao.selectByExampleAndRowBounds(new Example.Builder(UserCollection.class)
                        .where(WeekendSqls.<UserCollection>custom().andEqualTo(UserCollection::getUserId, reqDto.getUserId())
                                .andNotIn(UserCollection::getTypeId, typeIds))
                        .build(), new RowBounds(reqDto.getP(), reqDto.getS()));
                int count = dao.selectCountByExample(new Example.Builder(UserCollection.class)
                        .where(WeekendSqls.<UserCollection>custom().andEqualTo(UserCollection::getUserId, reqDto.getUserId())
                                .andNotIn(UserCollection::getTypeId, typeIds))
                        .build());
                dto.setCountPage(count / reqDto.getS());
                dto.setCount(count);
            } else {
                collections = dao.selectByExampleAndRowBounds(new Example.Builder(UserCollection.class)
                        .where(WeekendSqls.<UserCollection>custom().andEqualTo(UserCollection::getUserId, reqDto.getUserId())
                                .andEqualTo(UserCollection::getTypeId, type))
                        .build(), new RowBounds(reqDto.getP(), reqDto.getS()));
                int count = dao.selectCountByExample(new Example.Builder(UserCollection.class)
                        .where(WeekendSqls.<UserCollection>custom().andEqualTo(UserCollection::getUserId, reqDto.getUserId())
                                .andEqualTo(UserCollection::getTypeId, type))
                        .build());
                dto.setCountPage(count / reqDto.getS());
                dto.setCount(count);
            }
            dto.setSize(reqDto.getS());
            dto.setPage(reqDto.getP());
            List<Long> documentIds = collections.stream().map(d -> d.getDataId()).collect(Collectors.toList());
            dto.setStatus(true);
            if (Collections3.isNotEmpty(documentIds)) {
                Map<Long, UserCollection> map = Maps.newHashMap();
                collections.forEach(d -> {
                    map.put(d.getDataId(), d);
                });
                List<Document> documents = documentDao.selectByExample(new Example.Builder(Document.class)
                        .where(WeekendSqls.<Document>custom().andEqualTo(Document::getStatus, EntitySetting.COMMON_SUCCESS)
                                .andIn(Document::getId, documentIds))
                        .build());
                List<UserCollectionRspDto> list = Lists.newArrayList();
                documents.forEach(d -> {
                    if (map.containsKey(d.getId())) {
                        UserCollection userCollection = map.get(d.getId());
                        UserCollectionRspDto temp = BeanMapper.map(d, UserCollectionRspDto.class);
                        temp.setCollectionId(userCollection.getId());
                        temp.setTime(userCollection.getTime());
                        list.add(temp);
                    }
                });
                dto.setList(list);
            }
        }
        return dto;
    }


    public ListDto<StageSubjectDto> listStageAndSubject(Long userId) {
        ListDto<StageSubjectDto> dto = new ListDto<>(false);
        if (userId == null || userId == 0L) {
            dto.setMsg("参数为空");
        } else {
            List<UserCollection> collections = dao.selectByExample(new Example.Builder(UserCollection.class)
                    .where(WeekendSqls.<UserCollection>custom().andEqualTo(UserCollection::getUserId, userId)
                            .andEqualTo(UserCollection::getTypeId, EntitySetting.DATA_TYPE_QUESTION))
                    .build());
            dto.setStatus(true);
            List<Long> dataIds = collections.stream().map(d -> d.getDataId()).collect(Collectors.toList());
            if (Collections3.isNotEmpty(dataIds)) {
                List<Question> questions = questionDao.selectByExample(new Example.Builder(Question.class)
                        .where(WeekendSqls.<Question>custom().andEqualTo(Question::getStatus, EntitySetting.COMMON_SUCCESS)
                                .andIn(Question::getId, dataIds))
                        .selectDistinct("subjectId", "stage")
                        .build());
                Map<Integer, List<Long>> map = Maps.newHashMap();
                List<Long> subjectIds = Lists.newArrayList();
                questions.forEach(d -> {
                    List<Long> list;
                    if (map.containsKey(d.getStage())) {
                        list = map.get(d.getStage());
                    } else {
                        list = Lists.newArrayList();
                    }
                    list.add(d.getSubjectId());
                    map.put(d.getStage(), list);
                    subjectIds.add(d.getSubjectId());
                });
                List<Subjects> subjectss = subjectsDao.selectByExample(new Example.Builder(Subjects.class)
                        .where(WeekendSqls.<Subjects>custom()
                                .andIn(Subjects::getId, subjectIds))
                        .build());
                Map<Long, String> subjectName = Maps.newHashMap();
                subjectss.forEach(d -> {
                    subjectName.put(d.getId(), d.getName());
                });
                List<StageSubjectDto> list = Lists.newArrayList();
                map.forEach((k, v) -> {
                    StageSubjectDto stageSubjectDto = new StageSubjectDto();
                    stageSubjectDto.setStage(k);
                    List<CommonIdAndNameDto> subjects = Lists.newArrayList();
                    v.forEach(d -> {
                        if (subjectName.containsKey(d)) {
                            CommonIdAndNameDto temp = new CommonIdAndNameDto();
                            temp.setId(d);
                            temp.setName(subjectName.get(d));
                            subjects.add(temp);
                        }
                    });
                    stageSubjectDto.setSubjects(subjects);
                    list.add(stageSubjectDto);
                });
                dto.setList(list);
            }
        }
        return dto;
    }

    public PageDto<UserCollectionQuestionRspDto> listQuestionByUserCollection(UserCollectionQuestionReqDto reqDto) {
        PageDto<UserCollectionQuestionRspDto> dto = new PageDto<>(false);
        if (reqDto == null) {
            dto.setMsg("参数为空");
        } else if (reqDto.getUserId() == null || reqDto.getUserId() == 0L) {
            dto.setMsg("用户为空");
        } else {
            dto.setStatus(true);
            if (reqDto.getP() == null) {
                reqDto.setP(0);
            }
            if (reqDto.getS() == null) {
                reqDto.setS(10);
            }
            List<UserCollection>
                    collections = dao.selectByExampleAndRowBounds(new Example.Builder(UserCollection.class)
                    .where(WeekendSqls.<UserCollection>custom().andEqualTo(UserCollection::getUserId, reqDto.getUserId())
                            .andEqualTo(UserCollection::getTypeId, EntitySetting.DATA_TYPE_QUESTION))
                    .build(), new RowBounds(reqDto.getP(), reqDto.getS()));
            int count = dao.selectCountByExample(new Example.Builder(UserCollection.class)
                    .where(WeekendSqls.<UserCollection>custom().andEqualTo(UserCollection::getUserId, reqDto.getUserId())
                            .andEqualTo(UserCollection::getTypeId, EntitySetting.DATA_TYPE_QUESTION))
                    .build());
            dto.setSize(reqDto.getS());
            dto.setCountPage(count / reqDto.getS());
            dto.setCount(count);
            dto.setPage(reqDto.getP());
            List<Long> questionIds = collections.stream().map(d -> d.getDataId()).collect(Collectors.toList());
            dto.setStatus(true);
            if (Collections3.isNotEmpty(questionIds)) {
                Map<Long, UserCollection> map = Maps.newHashMap();
                collections.forEach(d -> {
                    map.put(d.getDataId(), d);
                });
                List<Question> questions = questionDao.selectByExample(new Example.Builder(Question.class)
                        .where(WeekendSqls.<Question>custom().andEqualTo(Question::getStatus, EntitySetting.COMMON_SUCCESS)
                                .andIn(Question::getId, questionIds))
                        .build());
                List<UserCollectionQuestionRspDto> list = Lists.newArrayList();
                questions.forEach(d -> {
                    if (map.containsKey(d.getId())) {
                        UserCollection userCollection = map.get(d.getId());
                        UserCollectionQuestionRspDto temp = BeanMapper.map(d, UserCollectionQuestionRspDto.class);
                        temp.setCollectionId(userCollection.getId());
                        list.add(temp);
                    }
                });
                dto.setList(list);
            }
        }
        return dto;
    }

    @Transactional
    public StatusDto deleteByUserAndQuestion(Long userId, List<Long> questionIds) {
        StatusDto dto = new StatusDto(false);
        if (userId == null || userId == 0L) {
            dto.setMsg("用户未传输");
        } else if (Collections3.isEmpty(questionIds)) {
            dto.setMsg("题目未传输");
        } else {
            User user = userDao.selectByPrimaryKey(userId);
            if (user == null) {
                dto.setMsg("用户不存在");
            } else {
                dto.setStatus(true);
                dao.deleteByExample(new Example.Builder(UserCollection.class)
                        .where(WeekendSqls.<UserCollection>custom()
                                .andEqualTo(UserCollection::getUserId, userId)
                                .andIn(UserCollection::getDataId, questionIds))
                        .build());
            }
        }
        return dto;
    }

    @Transactional
    public StatusDto updateByUserAndQuestion(Long userId, Long questionId) {
        StatusDto dto = new StatusDto(false);
        if (userId == null || userId == 0L) {
            dto.setMsg("用户未传输");
        } else if (questionId == null || questionId == 0L) {
            dto.setMsg("题目未传输");
        } else {
            User user = userDao.selectByPrimaryKey(userId);
            if (user == null) {
                dto.setMsg("用户不存在");
            } else {
                dto.setStatus(true);
                UserCollection userCollection = new UserCollection();
                userCollection.setDataId(questionId);
                userCollection.setUserId(userId);
                userCollection.setTypeId(EntitySetting.DATA_TYPE_QUESTION);
                userCollection = dao.selectOne(userCollection);
                if (userCollection == null) {
                    userCollection = new UserCollection();
                    userCollection.setDataId(questionId);
                    userCollection.setTypeId(EntitySetting.DATA_TYPE_QUESTION);
                    userCollection.setUserId(userId);
                    userCollection.setTime(System.currentTimeMillis());
                    super.create(userCollection);
                    dto.setCode("1");//收藏成功
                } else {
                    dto.setCode("2");//取消收藏成功
                    dao.delete(userCollection);
                }
            }
        }
        return dto;
    }
}