package com.ixinhoo.papers.service.user;

import com.chunecai.crumbs.api.client.ListDto;
import com.chunecai.crumbs.api.client.PageDto;
import com.chunecai.crumbs.api.client.StatusDto;
import com.chunecai.crumbs.api.dao.BaseDao;
import com.chunecai.crumbs.api.service.BaseService;
import com.chunecai.crumbs.code.util.collection.Collections3;
import com.chunecai.crumbs.code.util.mapper.BeanMapper;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ixinhoo.papers.dao.resources.DocumentDao;
import com.ixinhoo.papers.dao.user.PackagePaperDao;
import com.ixinhoo.papers.dao.user.UserOrderDao;
import com.ixinhoo.papers.dto.user.UserOrderListDto;
import com.ixinhoo.papers.dto.user.UserOrderPreNumStatusDto;
import com.ixinhoo.papers.dto.user.UserOrderReqDto;
import com.ixinhoo.papers.entity.EntitySetting;
import com.ixinhoo.papers.entity.resources.Document;
import com.ixinhoo.papers.entity.user.PackagePaper;
import com.ixinhoo.papers.entity.user.UserOrder;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.weekend.WeekendSqls;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserOrderService extends BaseService<UserOrder> {
    @Autowired
    private UserOrderDao dao;
    @Autowired
    private DocumentDao documentDao;
    @Autowired
    private PackagePaperDao packagePaperDao;
    @Override
    protected BaseDao getBaseDao() {
        return dao;
    }

    public PageDto<UserOrderListDto> pageUserOrderByType(UserOrderReqDto reqDto) {
        PageDto<UserOrderListDto> dto = new PageDto<>(true);
        if (reqDto != null) {
            List<UserOrderListDto> orderListDto = Lists.newArrayList();
            List<UserOrder> orderList;
            if (reqDto.getP() == null || reqDto.getS() == null) {
                if (reqDto.getBeginTime() == null || reqDto.getEndTime() == null) {
                    orderList = dao.selectByExample(new Example.Builder(UserOrder.class)
                            .where(WeekendSqls.<UserOrder>custom()
                                    .andIn(UserOrder::getType, reqDto.getType())
                                    .andEqualTo(UserOrder::getUserId, reqDto.getUserId())
                            ).orderByDesc("time").build());
                } else {
                    orderList = dao.selectByExample(new Example.Builder(UserOrder.class)
                            .where(WeekendSqls.<UserOrder>custom()
                                    .andIn(UserOrder::getType, reqDto.getType())
                                    .andEqualTo(UserOrder::getUserId, reqDto.getUserId())
                                    .andBetween(UserOrder::getTime, reqDto.getBeginTime(), reqDto.getEndTime())
                            ).orderByDesc("time").build());
                }
            } else {
                if (reqDto.getBeginTime() == null || reqDto.getEndTime() == null) {
                    orderList = dao.selectByExampleAndRowBounds(new Example.Builder(UserOrder.class)
                            .where(WeekendSqls.<UserOrder>custom()
                                    .andIn(UserOrder::getType, reqDto.getType())
                                    .andEqualTo(UserOrder::getUserId, reqDto.getUserId())
                            ).orderByDesc("time").build(), new RowBounds(reqDto.getP(), reqDto.getS()));
                    int count = dao.selectCountByExample(new Example.Builder(UserOrder.class)
                            .where(WeekendSqls.<UserOrder>custom()
                                    .andEqualTo(UserOrder::getUserId, reqDto.getUserId())
                                    .andIn(UserOrder::getType, reqDto.getType()))
                            .build());
                    dto.setCountPage(count / reqDto.getS());
                    dto.setCount(count);
                } else {
                    orderList = dao.selectByExampleAndRowBounds(new Example.Builder(UserOrder.class)
                            .where(WeekendSqls.<UserOrder>custom()
                                    .andIn(UserOrder::getType, reqDto.getType())
                                    .andEqualTo(UserOrder::getUserId, reqDto.getUserId())
                                    .andBetween(UserOrder::getTime, reqDto.getBeginTime(), reqDto.getEndTime())
                            ).orderByDesc("time").build(), new RowBounds(reqDto.getP(), reqDto.getS()));
                    int count = dao.selectCountByExample(new Example.Builder(UserOrder.class)
                            .where(WeekendSqls.<UserOrder>custom()
                                    .andEqualTo(UserOrder::getUserId, reqDto.getUserId())
                                    .andIn(UserOrder::getType, reqDto.getType())
                                    .andBetween(UserOrder::getTime, reqDto.getBeginTime(), reqDto.getEndTime()))
                            .build());
                    dto.setCountPage(count / reqDto.getS());
                    dto.setCount(count);
                }
            }
            dto.setSize(reqDto.getS());
            dto.setPage(reqDto.getP());
            if(Collections3.isNotEmpty(orderList)){
                Map<Long,Document> map = Maps.newHashMap();
                Map<Long,PackagePaper> paperMap = Maps.newHashMap();//组卷
                List<Long> documentIds = Lists.newArrayList();
                List<Long> papersIds = Lists.newArrayList();
                orderList.forEach(d->{
                    if(EntitySetting.USER_ORDER_TYPE_PAPERS==d.getType()){
                        papersIds.add(d.getDataId());
                    }else if(EntitySetting.USER_ORDER_TYPE_DOWNLOAD==d.getType()){
                        documentIds.add(d.getDataId());
                    }
                });
                if(Collections3.isNotEmpty(documentIds)&&reqDto.getType().contains(EntitySetting.USER_ORDER_TYPE_DOWNLOAD)){//资源类型
                   List<Document> documents = documentDao.selectByExample(new Example.Builder(Document.class)
                            .where(WeekendSqls.<Document>custom()
                            .andIn(Document::getId,documentIds)).build());
                   if(Collections3.isNotEmpty(documents)){
                       documents.forEach(d->{
                           map.put(d.getId(),d);
                       });
                   }
                }
                if(Collections3.isNotEmpty(papersIds)&&reqDto.getType().contains(EntitySetting.USER_ORDER_TYPE_DOWNLOAD)){//试卷类型
                   List<PackagePaper> packagePapers = packagePaperDao.selectByExample(new Example.Builder(PackagePaper.class)
                            .where(WeekendSqls.<PackagePaper>custom()
                            .andIn(PackagePaper::getId,papersIds)).build());
                   if(Collections3.isNotEmpty(packagePapers)){
                       packagePapers.forEach(d->{
                           paperMap.put(d.getId(),d);
                       });
                   }
                }
                orderList.forEach(d->{
                    UserOrderListDto temp = BeanMapper.map(d,UserOrderListDto.class);
                    if(EntitySetting.USER_ORDER_TYPE_PAPERS==d.getType()){
                        if(paperMap.containsKey(d.getDataId())){
                            temp.setDataDetail(paperMap.get(d.getDataId()));
                        }
                    }else if(EntitySetting.USER_ORDER_TYPE_DOWNLOAD==d.getType()){
                        if(map.containsKey(d.getDataId())){
                            temp.setDataDetail(map.get(d.getDataId()));
                        }
                    }
                    orderListDto.add(temp);
                });
            }
            dto.setList(orderListDto);
        }
        return dto;
    }

    public UserOrder findByOrderNum(String orderNum) {
        return dao.selectOneByExample(new Example.Builder(UserOrder.class)
                .where(WeekendSqls.<UserOrder>custom().andEqualTo(UserOrder::getOrderNum, orderNum)
                ).build());
    }

    public StatusDto findUserOrderStatusByPreNum(String preNum) {
        StatusDto dto = new StatusDto(false);
        if (Strings.isNullOrEmpty(preNum)) {
            dto.setMsg("参数为空");
        } else {
            UserOrder userOrder = findUserOrderByPreNum(preNum);
            if (userOrder != null) {
                if (EntitySetting.COMMON_SUCCESS == userOrder.getStatus()) {
                    dto.setStatus(true);
                }
            }
        }
        return dto;
    }

    public UserOrder findUserOrderByPreNum(String preNum) {
        return dao.selectOneByExample(new Example.Builder(UserOrder.class).
                    where(WeekendSqls.<UserOrder>custom()
                            .andEqualTo(UserOrder::getPreNum, preNum)).build());

    }

    public ListDto<UserOrderPreNumStatusDto> findUserOrderStatusByPreNumList(List<String> list) {
        ListDto<UserOrderPreNumStatusDto> dto = new ListDto<>(false);
        if (Collections3.isEmpty(list)) {
            dto.setMsg("数据为空");
        } else {
            List<UserOrder> userOrderList = dao.selectByExample(new Example.Builder(UserOrder.class).
                    where(WeekendSqls.<UserOrder>custom()
                            .andEqualTo(UserOrder::getStatus, EntitySetting.COMMON_SUCCESS)
                            .andIn(UserOrder::getPreNum, list)).build());
            List<String> successList = userOrderList.stream().map(d -> d.getPreNum()).collect(Collectors.toList());
            List<UserOrderPreNumStatusDto> dtoList = Lists.newArrayList();
            list.forEach(d -> {
                UserOrderPreNumStatusDto temp = new UserOrderPreNumStatusDto();
                temp.setPreNum(d);
                temp.setPreStatus(successList.contains(d));
                dtoList.add(temp);
            });
            dto.setStatus(true);
            dto.setList(dtoList);
        }
        return dto;
    }
}