package com.ixinhoo.papers.service.website;

import com.chunecai.crumbs.api.client.DetailDto;
import com.chunecai.crumbs.api.client.ListDto;
import com.chunecai.crumbs.api.client.StatusDto;
import com.chunecai.crumbs.api.code.DataTable;
import com.chunecai.crumbs.api.code.DataTableMeta;
import com.chunecai.crumbs.api.code.DataTableRequest;
import com.chunecai.crumbs.api.dao.BaseDao;
import com.chunecai.crumbs.api.service.BaseService;
import com.chunecai.crumbs.code.util.collection.Collections3;
import com.chunecai.crumbs.code.util.mapper.BeanMapper;
import com.chunecai.crumbs.code.util.string.StringUtil;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.ixinhoo.papers.dao.base.AreaDao;
import com.ixinhoo.papers.dao.base.SubjectsDao;
import com.ixinhoo.papers.dao.user.UserDao;
import com.ixinhoo.papers.dao.website.BannerRuleDao;
import com.ixinhoo.papers.dao.website.BannerRuleRangeDao;
import com.ixinhoo.papers.dao.website.BannerSettingDao;
import com.ixinhoo.papers.dto.website.BannerRuleDto;
import com.ixinhoo.papers.dto.website.BannerRuleRspDto;
import com.ixinhoo.papers.dto.website.BannerSettingDto;
import com.ixinhoo.papers.entity.EntitySetting;
import com.ixinhoo.papers.entity.base.Area;
import com.ixinhoo.papers.entity.base.Subjects;
import com.ixinhoo.papers.entity.user.User;
import com.ixinhoo.papers.entity.website.BannerRule;
import com.ixinhoo.papers.entity.website.BannerRuleRange;
import com.ixinhoo.papers.entity.website.BannerSetting;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.weekend.WeekendSqls;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BannerSettingService extends BaseService<BannerSetting> {
    @Autowired
    private BannerSettingDao dao;
    @Autowired
    private BannerRuleDao ruleDao;
    @Autowired
    private BannerRuleRangeDao rangeDao;
    @Autowired
    private SubjectsDao subjectsDao;
    @Autowired
    private AreaDao areaDao;
    @Autowired
    private UserDao userDao;

    @Override
    protected BaseDao getBaseDao() {
        return dao;
    }


    public ListDto<BannerSettingDto> listByStatusAndType(Long userId, Integer type) {
        ListDto<BannerSettingDto> dto = new ListDto<>();
        dto.setStatus(false);
        if (type == null) {
            dto.setMsg("类型不能为空");
        } else {
            List<BannerSetting> bannerSettings = dao.selectByExample(new Example.Builder(BannerSetting.class)
                    .where(WeekendSqls.<BannerSetting>custom()
                            .andEqualTo(BannerSetting::getStatus, EntitySetting.COMMON_SUCCESS)
                            .andEqualTo(BannerSetting::getType, type)
                    )
                    .build());
            dto.setStatus(true);
            bannerSettings.sort((h1, h2) -> h1.getSort().compareTo(h2.getSort()));
            List<BannerSettingDto> list = Lists.newArrayList();
            //TODO cici 待测
            //根据设置的规则进行获取
            User user = new User();
            if (userId != null && userId != 0L) {
                user = userDao.selectByPrimaryKey(userId);
            }
            for (BannerSetting d : bannerSettings) {
                List<BannerRule> rules = ruleDao.selectByExample(new Example.Builder(BannerRule.class)
                        .where(WeekendSqls.<BannerRule>custom()
                                .andEqualTo(BannerRule::getStatus, EntitySetting.COMMON_SUCCESS)
                                .andEqualTo(BannerRule::getBannerId, d.getId())
                                .andLessThanOrEqualTo(BannerRule::getBeginTime, System.currentTimeMillis())
                                .andGreaterThanOrEqualTo(BannerRule::getEndTime, System.currentTimeMillis())).build());
                if (Collections3.isNotEmpty(rules)) {
                    List<Long> ruleIds = rules.stream().map(d2 -> d2.getId()).collect(Collectors.toList());
                    List<BannerRuleRange> ruleRanges = rangeDao.selectByExample(new Example.Builder(BannerRuleRange.class)
                            .where(WeekendSqls.<BannerRuleRange>custom()
                                    .andIn(BannerRuleRange::getRuleId, ruleIds)).build());
                    if (Collections3.isNotEmpty(ruleRanges)) {
                        BannerRuleRange bannerRuleRange = null;
                        for (BannerRuleRange d2 : ruleRanges) {
                            if (d2.getStage() != null) {
                                List<String> s1 = Strings.isNullOrEmpty(d2.getStage()) ? Lists.newArrayList() : Arrays.asList(d2.getStage().split(","));
                                List<String> g1 = Strings.isNullOrEmpty(d2.getGrade()) ? Lists.newArrayList() : Arrays.asList(d2.getGrade().split(","));
                                List<String> s2 = Strings.isNullOrEmpty(d2.getSubject()) ? Lists.newArrayList() : Arrays.asList(d2.getSubject().split(","));
                                List<String> p1 = Strings.isNullOrEmpty(d2.getProvince()) ? Lists.newArrayList() : Arrays.asList(d2.getProvince().split(","));
                                List<String> t1 = Strings.isNullOrEmpty(d2.getTeachAge()) ? Lists.newArrayList() : Arrays.asList(d2.getTeachAge().split(","));
                                if (s1.contains("0")) {
                                    bannerRuleRange = d2;
                                    break;
                                } else if (user.getStage() != null && s1.contains(user.getStage())) {
                                    bannerRuleRange = d2;
                                    break;
                                } else if (g1.contains("0")) {
                                    bannerRuleRange = d2;
                                    break;
                                } else if (user.getGrade() != null && g1.contains(user.getGrade())) {
                                    bannerRuleRange = d2;
                                    break;
                                } else if (s2.contains("0")) {
                                    bannerRuleRange = d2;
                                    break;
                                } else if (user.getSubjectId() != null && s2.contains(user.getSubjectId())) {
                                    bannerRuleRange = d2;
                                    break;
                                } else if (p1.contains("0")) {
                                    bannerRuleRange = d2;
                                    break;
                                } else if (user.getProvinceId() != null && p1.contains(user.getProvinceId())) {
                                    bannerRuleRange = d2;
                                    break;
                                } else if (t1.contains("0")) {
                                    bannerRuleRange = d2;
                                    break;
                                } else if (user.getTeachingAge() != null && t1.contains(user.getTeachingAge())) {
                                    bannerRuleRange = d2;
                                    break;
                                }
                            }
                        }
                        if (bannerRuleRange != null) {
                            BannerSettingDto bannerSettingDto = new BannerSettingDto();
                            BannerRule rule = rules.get(0);
                            bannerSettingDto.setDataId(rule.getId());
                            bannerSettingDto.setId(d.getId());
                            bannerSettingDto.setImage(rule.getIcon());
                            bannerSettingDto.setLink(rule.getLink());
                            bannerSettingDto.setName(rule.getName());
                            bannerSettingDto.setType(d.getType());
                            bannerSettingDto.setSort(d.getSort());
                            list.add(bannerSettingDto);
                        }

                    }
                }
            }
            dto.setList(list);
        }
        return dto;
    }

    public DataTable<BannerRule> listBannerRuleDatatable(Long id, DataTableRequest dataTable) {
        DataTable<BannerRule> dto = new DataTable<>();
        DataTableMeta meta = new DataTableMeta();
        RowBounds rowBounds = null;
        Example example = new Example(BannerRule.class);
        if (dataTable != null) {
            rowBounds = new RowBounds(
                    (dataTable.getPagination().getPage() == 0
                            ? 0 : dataTable.getPagination().getPage() - 1)
                            * dataTable.getPagination().getPerpage(),
                    dataTable.getPagination().getPerpage());
            meta.setPerpage(dataTable.getPagination().getPerpage());
            meta.setPages(dataTable.getPagination().getPages());
            meta.setPage(dataTable.getPagination().getPage());
        }
        example.and().andEqualTo("bannerId", id);
        List<BannerRule> list = ruleDao.selectByExampleAndRowBounds(example, rowBounds);
        dto.setData(list);
        int count = ruleDao.selectCountByExample(example);
        meta.setTotal(count);
        dto.setMeta(meta);
        return dto;
    }

    @Transactional
    public StatusDto updateRuleStatusById(List<Long> ids, Integer status) {
        StatusDto dto = new StatusDto(false);
        if (Collections3.isEmpty(ids) || status == null || status == 0L) {
            dto.setMsg("参数不全");
        } else {
            ids.forEach(d -> {
                BannerRule bannerRule = ruleDao.selectByPrimaryKey(d);
                if (bannerRule != null) {
                    bannerRule.setStatus(status);
                    ruleDao.updateByPrimaryKey(bannerRule);
                }
            });
            dto.setStatus(true);
        }
        return dto;
    }

    @Transactional
    public StatusDto saveBannerRule(BannerRuleDto entity) {
        StatusDto dto = new StatusDto(false);
        if (entity == null) {
            dto.setMsg("参数为空");
        } else {
            BannerRule activity;
            if (entity.getId() == null || entity.getId() == 0L) {//新增
                entity.setStatus(EntitySetting.COMMON_SUCCESS);
                activity = BeanMapper.map(entity, BannerRule.class);
                ruleDao.insert(activity);
            } else {
                rangeDao.deleteByExample(new Example.Builder(BannerRuleRange.class)
                        .where(WeekendSqls.<BannerRuleRange>custom()
                                .andEqualTo(BannerRuleRange::getRuleId, entity.getId())).build());
                activity = BeanMapper.map(entity, BannerRule.class);
                ruleDao.updateByPrimaryKey(activity);
            }
            if (Collections3.isNotEmpty(entity.getStage()) || Collections3.isNotEmpty(entity.getGrade()) || Collections3.isNotEmpty(entity.getSubject()) || Collections3.isNotEmpty(entity.getProvince()) || Collections3.isNotEmpty(entity.getTeachAge())) {
                BannerRuleRange activityRange = new BannerRuleRange();
                if (Collections3.isNotEmpty(entity.getStage())) {
                    if (entity.getStage().contains(0)) {
                        activityRange.setStage("0,1,2,3");
                    } else {
                        activityRange.setStage(StringUtil.listJoinChart(entity.getStage(), ","));
                    }
                    if (Collections3.isNotEmpty(entity.getGrade())) {
                        if (entity.getGrade().contains(0)) {
                            activityRange.setGrade("0,1,2,3,4,5,6,7,8,9,10,11,12");
                        } else {
                            activityRange.setGrade(StringUtil.listJoinChart(entity.getGrade(), ","));
                        }
                    }
                }
                if (Collections3.isNotEmpty(entity.getSubject())) {
                    if (entity.getSubject().contains(0)) {
                        List<Subjects> subjectss = subjectsDao.selectAll();
                        List<Long> sids = subjectss.stream().map(d -> d.getId()).collect(Collectors.toList());
                        activityRange.setSubject("0," + StringUtil.listJoinChart(sids, ","));
                    } else {
                        activityRange.setSubject(StringUtil.listJoinChart(entity.getSubject(), ","));
                    }
                }
                if (Collections3.isNotEmpty(entity.getProvince())) {
                    if (entity.getProvince().contains(0)) {
                        List<Area> areas = areaDao.selectAll();
                        List<Long> sids = areas.stream().map(d -> d.getId()).collect(Collectors.toList());
                        activityRange.setProvince("0," + StringUtil.listJoinChart(sids, ","));
                    } else {
                        activityRange.setProvince(StringUtil.listJoinChart(entity.getProvince(), ","));
                    }
                }
                if (Collections3.isNotEmpty(entity.getTeachAge())) {
                    if (entity.getTeachAge().contains(0)) {
                        activityRange.setTeachAge("0,1,2,3,4");
                    } else {
                        activityRange.setTeachAge(StringUtil.listJoinChart(entity.getTeachAge(), ","));
                    }
                }
                activityRange.setRuleId(activity.getId());
                rangeDao.insert(activityRange);
            }
            dto.setStatus(true);
        }
        return dto;
    }

    @Transactional
    public StatusDto deleteBannerRuleByIds(List<Long> ids) {
        StatusDto dto = new StatusDto(false);
        if (Collections3.isEmpty(ids)) {
            dto.setMsg("参数为空");
        } else {
            ids.forEach(d -> {
                BannerRule bannerRule = ruleDao.selectByPrimaryKey(d);
                if (bannerRule != null) {
                    BannerRuleRange range = new BannerRuleRange();
                    range.setRuleId(d);
                    rangeDao.delete(range);
                    ruleDao.delete(bannerRule);
                }
            });
            dto.setStatus(true);
        }
        return dto;
    }

    public DetailDto<BannerRuleRspDto> findBannerRuleById(Long id) {
        DetailDto<BannerRuleRspDto> dto = new DetailDto<>(false);
        if (id == null || id == 0L) {
            dto.setMsg("参数为空");
        } else {
            BannerRule promotionActivity = ruleDao.selectByPrimaryKey(id);
            if (promotionActivity == null) {
                dto.setMsg("数据不存在");
            } else {
                BannerRuleRspDto promotionActivityDto = BeanMapper.map(promotionActivity, BannerRuleRspDto.class);
                //查询发送范围
                BannerRuleRange range = rangeDao.selectOneByExample(new Example.Builder(BannerRuleRange.class)
                        .where(WeekendSqls.<BannerRuleRange>custom()
                                .andEqualTo(BannerRuleRange::getRuleId, id)).build());
                if (range != null) {
                    promotionActivityDto.setGrade(range.getGrade());
                    promotionActivityDto.setStage(range.getStage());
                    promotionActivityDto.setSubject(range.getSubject());
                    promotionActivityDto.setProvince(range.getProvince());
                    promotionActivityDto.setTeachAge(range.getTeachAge());
                }
                dto.setDetail(promotionActivityDto);
                dto.setStatus(true);
            }
        }
        return dto;
    }
}