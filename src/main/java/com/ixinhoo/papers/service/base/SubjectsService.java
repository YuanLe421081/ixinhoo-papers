package com.ixinhoo.papers.service.base;

import com.chunecai.crumbs.api.client.ListDto;
import com.chunecai.crumbs.api.client.StatusDto;
import com.chunecai.crumbs.api.code.DataTable;
import com.chunecai.crumbs.api.code.DataTableMeta;
import com.chunecai.crumbs.api.code.DataTableRequest;
import com.chunecai.crumbs.api.dao.BaseDao;
import com.chunecai.crumbs.api.service.BaseService;
import com.chunecai.crumbs.api.shiro.ShiroUser;
import com.chunecai.crumbs.api.util.UsersUtil;
import com.chunecai.crumbs.code.database.SearchFilter;
import com.chunecai.crumbs.code.util.collection.Collections3;
import com.chunecai.crumbs.code.util.mapper.JsonMapper;
import com.cnjy21.api.constant.APIConstant;
import com.cnjy21.api.model.common.Subject;
import com.cnjy21.api.service.APIDocumentService;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.ixinhoo.papers.dao.base.StageSubjectsDao;
import com.ixinhoo.papers.dao.base.SubjectsDao;
import com.ixinhoo.papers.dao.common.InterfaceInfoDao;
import com.ixinhoo.papers.dto.common.CommonIdAndNameDto;
import com.ixinhoo.papers.entity.EntitySetting;
import com.ixinhoo.papers.entity.base.StageSubjects;
import com.ixinhoo.papers.entity.base.Subjects;
import com.ixinhoo.papers.entity.common.InterfaceInfo;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SubjectsService extends BaseService<Subjects> {
    @Autowired
    private SubjectsDao dao;
    @Autowired
    private InterfaceInfoDao interfaceInfoDao;
    @Autowired
    private StageSubjectsDao stageSubjectsDao;

    @Override
    protected BaseDao getBaseDao() {
        return dao;
    }

    @Transactional
    public StatusDto syncSubjects() {
        StatusDto dto = new StatusDto(true);
        APIDocumentService service = new APIDocumentService();
        //清空学段重新添加
        stageSubjectsDao.delete(null);
        //查询所有的学科信息
        List<Subjects> dbSubjects = dao.selectAll();
        List<Long> dbIds = dbSubjects.stream().map(d -> d.getId()).collect(Collectors.toList());
        // 获取小学教材科目
        syncStageAndSubjects(service, dbIds, APIConstant.STAGE_XIAOXUE);
        //获取初中教材科目
        syncStageAndSubjects(service, dbIds, APIConstant.STAGE_CHUZHONG);
        //获取高中教材科目
        syncStageAndSubjects(service, dbIds, APIConstant.STAGE_GAOZHONG);
        return dto;
    }

    @Transactional
    private void syncStageAndSubjects(APIDocumentService service, List<Long> dbIds, Integer type) {
        // 获取小学教材科目
        List<Subject> subjects = service.getSubjects(type);
        if (Collections3.isNotEmpty(subjects)) {
            InterfaceInfo interfaceInfo = new InterfaceInfo();
            interfaceInfo.setTime(System.currentTimeMillis());
            interfaceInfo.setReqData(type.toString());
            interfaceInfo.setType(EntitySetting.INTERFACE_SUBJECTS);
            interfaceInfo.setRspData(JsonMapper.nonDefaultMapper().toJson(subjects));
            ShiroUser user = UsersUtil.ShiroUser();
            if (user != null) {
                interfaceInfo.setCreateId(user.getId());
                interfaceInfo.setUpdateId(user.getId());
            }
            interfaceInfo.setCreateTime(System.currentTimeMillis());
            interfaceInfo.setUpdateTime(System.currentTimeMillis());
            interfaceInfoDao.insert(interfaceInfo);
            List<Subjects> list = Lists.newArrayList();
            List<StageSubjects> stageList = Lists.newArrayList();
            subjects.forEach(d -> {
                if (!dbIds.contains(d.getSubjectId().longValue())) {
                    Subjects temp = new Subjects();
                    temp.setId(d.getSubjectId().longValue());
                    temp.setName(d.getSubjectName());
                    temp.setCode(d.getSubjectId().toString());
                    if (user != null) {
                        temp.setCreateId(user.getId());
                        temp.setUpdateId(user.getId());
                    }
                    temp.setCreateTime(System.currentTimeMillis());
                    temp.setUpdateTime(System.currentTimeMillis());
                    dbIds.add(d.getSubjectId().longValue());
                    list.add(temp);
                }
                StageSubjects temp = new StageSubjects();
                temp.setName(d.getSubjectName());
                temp.setCode(d.getSubjectId().toString());
                temp.setStage(type);
                temp.setSubjectId(d.getSubjectId().longValue());
                if (user != null) {
                    temp.setCreateId(user.getId());
                    temp.setUpdateId(user.getId());
                }
                temp.setCreateTime(System.currentTimeMillis());
                temp.setUpdateTime(System.currentTimeMillis());
                stageList.add(temp);
            });
            stageSubjectsDao.insertList(stageList);
            if (Collections3.isNotEmpty(list)) {
                dao.insertList(list);
            }
        }
    }

    public DataTable<StageSubjects> listStageDatatable(DataTableRequest dataTable) {
        DataTable<StageSubjects> dto = new DataTable<>();
        DataTableMeta meta = new DataTableMeta();
        RowBounds rowBounds = null;
        Example example = new Example(StageSubjects.class);
        if (dataTable != null) {
            rowBounds = new RowBounds(
                    (dataTable.getPagination().getPage() == 0
                            ? 0 : dataTable.getPagination().getPage() - 1)
                            * dataTable.getPagination().getPerpage(),
                    dataTable.getPagination().getPerpage());
            meta.setPerpage(dataTable.getPagination().getPerpage());
            meta.setPages(dataTable.getPagination().getPages());
            meta.setPage(dataTable.getPagination().getPage());
            if (!Strings.isNullOrEmpty(dataTable.getPagination().getField())) {
                example.setOrderByClause(dataTable.getPagination().getField() + " " + dataTable.getPagination().getSort());
            }
            Map<String, Object> searchMap = dataTable.getSearchMap();
            if (searchMap != null && searchMap.size() != 0) {
                Map<String, SearchFilter> searchFilters = SearchFilter.parse(searchMap);
                if (searchFilters != null) {
                    example.createCriteria();
                    searchFilters.forEach((k, v) -> {
                        if (!Strings.isNullOrEmpty(k) && v != null && v.value != null && !Strings.isNullOrEmpty(v.fieldName)) {
                            if (SearchFilter.Operator.EQ.equals(v.operator)) {
                                example.and().andEqualTo(v.fieldName, v.value);
                            } else if (SearchFilter.Operator.NOT.equals(v.operator)) {
                                example.and().andNotEqualTo(v.fieldName, v.value);
                            } else if (SearchFilter.Operator.LIKE.equals(v.operator)) {
                                example.and().andLike(v.fieldName, v.value.toString());
                            } else if (SearchFilter.Operator.LIKE.equals(v.operator)) {
                                example.and().andLike(v.fieldName, v.value.toString());
                            } else if (SearchFilter.Operator.LT.equals(v.operator)) {
                                example.and().andLessThan(v.fieldName, v.value);
                            } else if (SearchFilter.Operator.LTE.equals(v.operator)) {
                                example.and().andLessThanOrEqualTo(v.fieldName, v.value);
                            } else if (SearchFilter.Operator.GT.equals(v.operator)) {
                                example.and().andGreaterThan(v.fieldName, v.value);
                            } else if (SearchFilter.Operator.GTE.equals(v.operator)) {
                                example.and().andGreaterThanOrEqualTo(v.fieldName, v.value);
                            }
                        }
                    });
                }
            }
        }
        List<StageSubjects> list = stageSubjectsDao.selectByExampleAndRowBounds(example, rowBounds);
        dto.setData(list);
        int count = stageSubjectsDao.selectCountByExample(example);
        meta.setTotal(count);
        dto.setMeta(meta);
        return dto;
    }

    public ListDto<CommonIdAndNameDto> findByStage(Integer stage) {
        ListDto<CommonIdAndNameDto> dto = new ListDto<>(true);
        List<CommonIdAndNameDto> list = Lists.newArrayList();
        if (stage == null || stage == 0L) {
            //查询所有
            List<Subjects> subjectss = dao.selectAll();
            subjectss.forEach(d -> {
                CommonIdAndNameDto temp = new CommonIdAndNameDto();
                temp.setId(d.getId());
                temp.setName(d.getName());
                list.add(temp);
            });
        } else {
            StageSubjects stageSubjects = new StageSubjects();
            stageSubjects.setStage(stage);
            List<StageSubjects> stageSubjectss = stageSubjectsDao.select(stageSubjects);
            stageSubjectss.forEach(d -> {
                CommonIdAndNameDto temp = new CommonIdAndNameDto();
                temp.setId(d.getSubjectId());
                temp.setName(d.getName());
                list.add(temp);
            });
        }
        dto.setList(list);
        return dto;
    }

    public ListDto<Subjects> listAll() {
        return new ListDto<>(true, dao.selectAll());
    }
}