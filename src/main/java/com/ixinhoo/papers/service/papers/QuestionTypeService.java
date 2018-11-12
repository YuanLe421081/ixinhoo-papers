package com.ixinhoo.papers.service.papers;

import com.chunecai.crumbs.api.client.ListDto;
import com.chunecai.crumbs.api.client.StatusDto;
import com.chunecai.crumbs.api.code.DataTable;
import com.chunecai.crumbs.api.code.DataTableRequest;
import com.chunecai.crumbs.api.dao.BaseDao;
import com.chunecai.crumbs.api.service.BaseService;
import com.chunecai.crumbs.api.shiro.ShiroUser;
import com.chunecai.crumbs.api.util.UsersUtil;
import com.chunecai.crumbs.code.util.collection.Collections3;
import com.chunecai.crumbs.code.util.mapper.JsonMapper;
import com.cnjy21.api.service.APIQuestionService;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.ixinhoo.papers.dao.base.StageSubjectsDao;
import com.ixinhoo.papers.dao.common.InterfaceInfoDao;
import com.ixinhoo.papers.dao.papers.QuestionTypeDao;
import com.ixinhoo.papers.dao.papers.SubjectQuestionTypeDao;
import com.ixinhoo.papers.dto.common.CommonIdAndNameDto;
import com.ixinhoo.papers.entity.EntitySetting;
import com.ixinhoo.papers.entity.base.StageSubjects;
import com.ixinhoo.papers.entity.common.InterfaceInfo;
import com.ixinhoo.papers.entity.papers.QuestionType;
import com.ixinhoo.papers.entity.papers.SubjectQuestionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class QuestionTypeService extends BaseService<QuestionType> {
    @Autowired
    private QuestionTypeDao dao;
    @Autowired
    private SubjectQuestionTypeDao subjectQuestionTypeDao;
    @Autowired
    private InterfaceInfoDao interfaceInfoDao;
    @Autowired
    private StageSubjectsDao stageSubjectsDao;

    @Override
    protected BaseDao getBaseDao() {
        return dao;
    }

    /**
     * 清空同步--id自增，questionType的id与21世纪一致;
     * SubjectQuestionType --id自增
     *
     * @return
     */
    @Transactional
    public StatusDto syncQuestionType() {
        StatusDto dto = new StatusDto(false);
        APIQuestionService service = new APIQuestionService();
        //查询学段学科信息
        List<StageSubjects> stageSubjectss = stageSubjectsDao.selectAll();
        if (Collections3.isEmpty(stageSubjectss)) {
            dto.setMsg("学段学科信息为空,请先同步学段学科信息");
        } else {
            //清空题目题型重新添加
            dao.delete(null);
            subjectQuestionTypeDao.delete(null);
            List<Long> idsList = Lists.newArrayList();
            for (StageSubjects d : stageSubjectss) {
                idsList = syncQuestionTypeByStageAndSubject(service, d, idsList);
            }
            dto.setStatus(true);
        }
        return dto;
    }

    @Transactional
    private List<Long> syncQuestionTypeByStageAndSubject(APIQuestionService service, StageSubjects stageSubjects, List<Long> idsList) {
        if (stageSubjects != null && stageSubjects.getStage() != null && !Strings.isNullOrEmpty(stageSubjects.getCode()) && stageSubjects.getCode().matches("[\\d]+")) {
            List<com.cnjy21.api.model.question.QuestionType> questionTypes = service.getQuestionTypes(stageSubjects.getStage(), Integer.parseInt(stageSubjects.getCode()));
            if (Collections3.isNotEmpty(questionTypes)) {
                InterfaceInfo interfaceInfo = new InterfaceInfo();
                interfaceInfo.setTime(System.currentTimeMillis());
                interfaceInfo.setReqData("{\"stage\":" + stageSubjects.getStage() + ",\"subjectId\":" + stageSubjects.getCode() + "}");
                interfaceInfo.setType(EntitySetting.INTERFACE_QUESTION_TYPE);
                interfaceInfo.setRspData(JsonMapper.nonDefaultMapper().toJson(questionTypes));
                ShiroUser user = UsersUtil.ShiroUser();
                if (user != null) {
                    interfaceInfo.setCreateId(user.getId());
                    interfaceInfo.setUpdateId(user.getId());
                }
                interfaceInfo.setCreateTime(System.currentTimeMillis());
                interfaceInfo.setUpdateTime(System.currentTimeMillis());
                interfaceInfoDao.insert(interfaceInfo);
                List<QuestionType> list = Lists.newArrayList();
                List<SubjectQuestionType> questionTypeList = Lists.newArrayList();
                questionTypes.forEach(d -> {
                    if (!idsList.contains(d.getType().longValue())) {
                        QuestionType temp = new QuestionType();
                        temp.setName(d.getTypeName());
                        temp.setCode(d.getType().toString());
                        temp.setId(d.getType().longValue());
                        if (user != null) {
                            temp.setCreateId(user.getId());
                            temp.setUpdateId(user.getId());
                        }
                        temp.setCreateTime(System.currentTimeMillis());
                        temp.setUpdateTime(System.currentTimeMillis());
                        list.add(temp);
                        idsList.add(temp.getId());
                    }
                    SubjectQuestionType temp = new SubjectQuestionType();
                    temp.setSubjectName(stageSubjects.getName());
                    temp.setSubjectId(Long.parseLong(stageSubjects.getCode()));
                    temp.setName(d.getTypeName());
                    temp.setCode(d.getType().toString());
                    temp.setStage(stageSubjects.getStage());
                    if (user != null) {
                        temp.setCreateId(user.getId());
                        temp.setUpdateId(user.getId());
                    }
                    temp.setCreateTime(System.currentTimeMillis());
                    temp.setUpdateTime(System.currentTimeMillis());
                    questionTypeList.add(temp);
                });
                if (Collections3.isNotEmpty(list)) {
                    dao.insertList(list);
                }
                if (Collections3.isNotEmpty(questionTypeList)) {
                    subjectQuestionTypeDao.insertList(questionTypeList);
                }
            }
        }
        return idsList;
    }


    public ListDto<CommonIdAndNameDto> findAll() {
        ListDto<CommonIdAndNameDto> dto = new ListDto<>(true);
        List<QuestionType> questionTypes = dao.selectAll();
        List<CommonIdAndNameDto> list = Lists.newArrayList();
        questionTypes.forEach(d -> {
            CommonIdAndNameDto temp = new CommonIdAndNameDto();
            temp.setId(d.getId());
            temp.setName(d.getName());
            list.add(temp);
        });
        dto.setList(list);
        return dto;
    }

}