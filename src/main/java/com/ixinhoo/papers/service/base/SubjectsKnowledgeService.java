package com.ixinhoo.papers.service.base;

import com.chunecai.crumbs.api.client.ListDto;
import com.chunecai.crumbs.api.client.StatusDto;
import com.chunecai.crumbs.api.dao.BaseDao;
import com.chunecai.crumbs.api.service.BaseService;
import com.chunecai.crumbs.api.shiro.ShiroUser;
import com.chunecai.crumbs.api.util.UsersUtil;
import com.chunecai.crumbs.code.util.collection.Collections3;
import com.chunecai.crumbs.code.util.mapper.BeanMapper;
import com.chunecai.crumbs.code.util.mapper.JsonMapper;
import com.cnjy21.api.model.common.KnowledgePoint;
import com.cnjy21.api.service.APIDocumentService;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.ixinhoo.papers.dao.base.StageSubjectsDao;
import com.ixinhoo.papers.dao.base.SubjectsKnowledgeDao;
import com.ixinhoo.papers.dao.common.InterfaceInfoDao;
import com.ixinhoo.papers.dto.tree.CommonTreeDto;
import com.ixinhoo.papers.dto.tree.TreeDto;
import com.ixinhoo.papers.dto.tree.TreeNodeDto;
import com.ixinhoo.papers.entity.EntitySetting;
import com.ixinhoo.papers.entity.base.StageSubjects;
import com.ixinhoo.papers.entity.base.SubjectsKnowledge;
import com.ixinhoo.papers.entity.common.InterfaceInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubjectsKnowledgeService extends BaseService<SubjectsKnowledge> {
    @Autowired
    private SubjectsKnowledgeDao dao;
    @Autowired
    private InterfaceInfoDao interfaceInfoDao;
    @Autowired
    private StageSubjectsDao stageSubjectsDao;

    @Override
    protected BaseDao getBaseDao() {
        return dao;
    }

    @Transactional
    public StatusDto syncSubjectsKnowledge() {
        StatusDto dto = new StatusDto(false);
        APIDocumentService service = new APIDocumentService();
        //查询学段学科信息
        List<StageSubjects> stageSubjectss = stageSubjectsDao.selectAll();
        if (Collections3.isEmpty(stageSubjectss)) {
            dto.setMsg("学段学科信息为空,请先同步学段学科信息");
        } else {
            //清空知识点重新添加
            dao.delete(null);
            stageSubjectss.forEach(d -> {
                syncSubjectsKnowledgeByStageAndSubject(service, d.getStage(), d.getCode(), d.getName());
            });
            dto.setStatus(true);
        }
        return dto;
    }

    @Transactional
    private void syncSubjectsKnowledgeByStageAndSubject(APIDocumentService service, Integer stage, String subject, String name) {
        if (!Strings.isNullOrEmpty(subject) && subject.matches("[\\d]+")) {
            List<KnowledgePoint> knowledgePoints = service.getKnowledgePoints(stage, Integer.parseInt(subject));
            if (Collections3.isNotEmpty(knowledgePoints)) {
                InterfaceInfo interfaceInfo = new InterfaceInfo();
                interfaceInfo.setTime(System.currentTimeMillis());
                interfaceInfo.setReqData("{\"stage\":" + stage + ",\"subjectId\":" + subject + "}");
                interfaceInfo.setType(EntitySetting.INTERFACE_KNOWLEDGE);
                interfaceInfo.setRspData(JsonMapper.nonDefaultMapper().toJson(knowledgePoints));
                ShiroUser user = UsersUtil.ShiroUser();
                if (user != null) {
                    interfaceInfo.setCreateId(user.getId());
                    interfaceInfo.setUpdateId(user.getId());
                }
                interfaceInfo.setCreateTime(System.currentTimeMillis());
                interfaceInfo.setUpdateTime(System.currentTimeMillis());
                interfaceInfoDao.insert(interfaceInfo);
                List<SubjectsKnowledge> list = subjectsKnowledgeList(knowledgePoints, name);
                if (Collections3.isNotEmpty(list)) {
                    dao.insertList(list);
                }
            }
        }
    }


    private List<SubjectsKnowledge> subjectsKnowledgeList(List<KnowledgePoint> knowledgePoints, String name) {
        List<SubjectsKnowledge> list = Lists.newArrayList();
        knowledgePoints.forEach(d -> {
            SubjectsKnowledge temp = new SubjectsKnowledge();
            temp.setSubjectName(name);
            temp.setName(d.getName());
            temp.setCode(d.getId().toString());
            temp.setId(d.getId().longValue());
            temp.setStage(d.getStage());
            temp.setSubjectId(d.getSubjectId().longValue());
            temp.setSupId(d.getUpid() == null ? 0L : d.getUpid().longValue());
            temp.setPath(d.getPath());
            temp.setSort(d.getSort() == null ? 0L : d.getSort().longValue());
            ShiroUser user = UsersUtil.ShiroUser();
            if (user != null) {
                temp.setCreateId(user.getId());
                temp.setUpdateId(user.getId());
            }
            temp.setCreateTime(System.currentTimeMillis());
            temp.setUpdateTime(System.currentTimeMillis());
            list.add(temp);
            if (Collections3.isNotEmpty(d.getChilds())) {
                list.addAll(subjectsKnowledgeList(d.getChilds(), name));
            }
        });
        return list;
    }

    public ListDto<CommonTreeDto> findSubjectKnowledgeTree(Integer stage, Long subjectId) {
        ListDto<CommonTreeDto> dto = new ListDto<>(true);
        if (stage == null || stage == 0L) {
            dto.setMsg("学段信息为空");
        } else {
            SubjectsKnowledge subjectsKnowledge = new SubjectsKnowledge();
            if (subjectId != null && subjectId != 0L) {
                subjectsKnowledge.setSubjectId(subjectId);
            }
            if (stage != null) {
                subjectsKnowledge.setStage(stage);
            }
            List<SubjectsKnowledge> subjectsKnowledges = dao.select(subjectsKnowledge);
            if (Collections3.isNotEmpty(subjectsKnowledges)) {
                List<Long> subjectIds = subjectsKnowledges.stream().map(d -> d.getId()).collect(Collectors.toList());
                List<TreeNodeDto> nodeDtoList = Lists.newArrayList();
                subjectsKnowledges.forEach(d -> {
                    TreeNodeDto temp = new TreeNodeDto();
                    temp.setCode(d.getId());
                    temp.setName(d.getName());
                    if (subjectIds.contains(d.getSupId())) {
                        temp.setParent(d.getSupId());
                    }
                    nodeDtoList.add(temp);
                });
                TreeDto treeDto = new TreeDto(nodeDtoList);
                dto.setList(BeanMapper.mapList(treeDto.getRoot(), CommonTreeDto.class));
            }
        }
        return dto;
    }
}