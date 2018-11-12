package com.ixinhoo.papers.service.website;

import com.chunecai.crumbs.api.client.DetailDto;
import com.chunecai.crumbs.api.client.StatusDto;
import com.chunecai.crumbs.api.dao.BaseDao;
import com.chunecai.crumbs.api.service.BaseService;
import com.chunecai.crumbs.code.util.collection.Collections3;
import com.chunecai.crumbs.code.util.mapper.BeanMapper;
import com.chunecai.crumbs.code.util.string.StringUtil;
import com.ixinhoo.papers.dao.base.AreaDao;
import com.ixinhoo.papers.dao.base.SubjectsDao;
import com.ixinhoo.papers.dao.common.MessageNoticeDao;
import com.ixinhoo.papers.dao.website.ActivityRangeDao;
import com.ixinhoo.papers.dao.website.PromotionActivityDao;
import com.ixinhoo.papers.dto.website.PromotionActivityDto;
import com.ixinhoo.papers.dto.website.PromotionActivityRspDto;
import com.ixinhoo.papers.entity.EntitySetting;
import com.ixinhoo.papers.entity.base.Area;
import com.ixinhoo.papers.entity.base.Subjects;
import com.ixinhoo.papers.entity.common.MessageNotice;
import com.ixinhoo.papers.entity.website.ActivityRange;
import com.ixinhoo.papers.entity.website.PromotionActivity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.weekend.WeekendSqls;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PromotionActivityService extends BaseService<PromotionActivity> {
    @Autowired
    private PromotionActivityDao dao;
    @Autowired
    private ActivityRangeDao rangeDao;
    @Autowired
    private MessageNoticeDao messageNoticeDao;
    @Autowired
    private SubjectsDao subjectsDao;
    @Autowired
    private AreaDao areaDao;

    @Override
    protected BaseDao getBaseDao() {
        return dao;
    }

    @Transactional
    public StatusDto updateStatusByIds(List<Long> ids, Integer status) {
        StatusDto dto = new StatusDto(false);
        if (Collections3.isEmpty(ids) || status == null || status == 0L) {
            dto.setMsg("参数为空");
        } else {
            List<PromotionActivity> list = dao.selectByExample(new Example.Builder(PromotionActivity.class)
                    .where(WeekendSqls.<PromotionActivity>custom()
                            .andIn(PromotionActivity::getId, ids)).build());
            if (Collections3.isNotEmpty(list)) {
                list.forEach(d -> {
                    d.setStatus(status);
                    super.updateById(d);
                });
            }
        }
        return dto;
    }

    @Transactional
    public StatusDto savePromotionActivity(PromotionActivityDto entity) {
        StatusDto dto = new StatusDto(false);
        if (entity == null) {
            dto.setMsg("参数为空");
        } else {
            if (entity.getId() == null || entity.getId() == 0L) {//新增
                entity.setStatus(EntitySetting.COMMON_SUCCESS);
            } else {
                if (entity.getNoticeId() != null && entity.getNoticeId() != 0L) {
                    messageNoticeDao.deleteByPrimaryKey(entity.getNoticeId());
                }
                rangeDao.deleteByExample(new Example.Builder(ActivityRange.class)
                        .where(WeekendSqls.<ActivityRange>custom()
                                .andEqualTo(ActivityRange::getPromotionId, entity.getId())).build());
            }
            if (EntitySetting.COMMON_SUCCESS == entity.getSendMessage()) {//发送站内信
                MessageNotice messageNotice = new MessageNotice();
                messageNotice.setType(EntitySetting.MESSAGE_NOTICE_TYPE_ACTIVITY);
                messageNotice.setContent(entity.getNoticeContent());
                messageNotice.setTitle(entity.getNoticeTitle());
                messageNotice.setPushType(EntitySetting.MESSAGE_NOTICE_PUSH_TYPE_RANG);
                messageNoticeDao.insert(messageNotice);
                entity.setNoticeId(messageNotice.getId());
            }
            PromotionActivity activity = BeanMapper.map(entity, PromotionActivity.class);
            super.save(activity);
            if (Collections3.isNotEmpty(entity.getStage()) || Collections3.isNotEmpty(entity.getGrade()) || Collections3.isNotEmpty(entity.getSubject()) || Collections3.isNotEmpty(entity.getProvince())) {
                ActivityRange activityRange = new ActivityRange();
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
                        activityRange.setSubject("0,"+StringUtil.listJoinChart(sids, ","));
                    } else {
                        activityRange.setSubject(StringUtil.listJoinChart(entity.getSubject(), ","));
                    }
                }
                if (Collections3.isNotEmpty(entity.getProvince())) {
                    if (entity.getProvince().contains(0)) {
                        List<Area> areas = areaDao.selectAll();
                        List<Long> sids = areas.stream().map(d -> d.getId()).collect(Collectors.toList());
                        activityRange.setProvince("0,"+StringUtil.listJoinChart(sids, ","));
                    } else {
                        activityRange.setProvince(StringUtil.listJoinChart(entity.getProvince(), ","));
                    }
                }
                activityRange.setPromotionId(activity.getId());
                rangeDao.insert(activityRange);
            }
            dto.setStatus(true);
        }
        return dto;
    }

    public DetailDto<PromotionActivityRspDto> findPromotionById(Long id) {
        DetailDto<PromotionActivityRspDto> dto = new DetailDto<>(false);
        if (id == null || id == 0L) {
            dto.setMsg("参数为空");
        } else {
            PromotionActivity promotionActivity = findById(id);
            if (promotionActivity == null) {
                dto.setMsg("数据不存在");
            } else {
                PromotionActivityRspDto promotionActivityDto = BeanMapper.map(promotionActivity, PromotionActivityRspDto.class);
                //判断是否有通知，查询通知内容：
                if (EntitySetting.COMMON_SUCCESS == promotionActivity.getSendMessage() && promotionActivity.getNoticeId() != null && promotionActivity.getNoticeId() != 0L) {
                    MessageNotice messageNotice = messageNoticeDao.selectByPrimaryKey(promotionActivity.getNoticeId());
                    if (messageNotice != null) {
                        promotionActivityDto.setNoticeId(messageNotice.getId());
                        promotionActivityDto.setNoticeTitle(messageNotice.getTitle());
                        promotionActivityDto.setNoticeContent(messageNotice.getContent());
                    }
                }
                //查询发送范围
                ActivityRange range = rangeDao.selectOneByExample(new Example.Builder(ActivityRange.class)
                        .where(WeekendSqls.<ActivityRange>custom()
                                .andEqualTo(ActivityRange::getPromotionId, id)).build());
                if (range != null) {
                    promotionActivityDto.setGrade(range.getGrade());
                    promotionActivityDto.setStage(range.getStage());
                    promotionActivityDto.setSubject(range.getSubject());
                    promotionActivityDto.setProvince(range.getProvince());
                }
                dto.setDetail(promotionActivityDto);
                dto.setStatus(true);
            }
        }
        return dto;
    }

    @Transactional
    public StatusDto deletePromotionByIds(List<Long> ids) {
        StatusDto dto = new StatusDto(false);
        if (Collections3.isEmpty(ids)) {
            dto.setMsg("参数为空");
        } else {
            ids.forEach(d -> {
                PromotionActivity promotionActivity = findById(d);
                if (promotionActivity != null) {
                    if (promotionActivity.getNoticeId() != null && promotionActivity.getNoticeId() != 0L) {
                        messageNoticeDao.deleteByPrimaryKey(promotionActivity.getNoticeId());
                    }
                    deleteById(d);
                    ActivityRange range = new ActivityRange();
                    range.setPromotionId(d);
                    rangeDao.deleteByExample(range);
                }
            });
        }
        return dto;
    }
}