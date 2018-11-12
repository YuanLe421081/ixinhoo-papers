package com.ixinhoo.papers.service.website;

import com.chunecai.crumbs.api.client.DetailDto;
import com.chunecai.crumbs.api.client.ListDto;
import com.chunecai.crumbs.code.util.collection.Collections3;
import com.chunecai.crumbs.code.util.date.DateUtil;
import com.chunecai.crumbs.code.util.mapper.BeanMapper;
import com.google.common.collect.Lists;
import com.ixinhoo.papers.dao.base.ChaptersDao;
import com.ixinhoo.papers.dao.resources.DocumentDao;
import com.ixinhoo.papers.dao.user.UserDao;
import com.ixinhoo.papers.dao.user.UserDownloadDao;
import com.ixinhoo.papers.dto.website.HomeRecommendDto;
import com.ixinhoo.papers.dto.website.HomeWebsiteDto;
import com.ixinhoo.papers.entity.EntitySetting;
import com.ixinhoo.papers.entity.resources.Document;
import com.ixinhoo.papers.entity.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.weekend.WeekendSqls;

import java.util.Calendar;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class WebsiteService {
    @Autowired
    private DocumentDao documentDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private UserDownloadDao userDownloadDao;
    @Autowired
    private ChaptersDao chaptersDao;


    public DetailDto<HomeWebsiteDto> homeWebsite() {
        DetailDto<HomeWebsiteDto> dto = new DetailDto<>(true);
        HomeWebsiteDto homeWebsiteDto = new HomeWebsiteDto();
        Document document = new Document();
        document.setStatus(EntitySetting.COMMON_SUCCESS);
        homeWebsiteDto.setDocumentNum(documentDao.selectCount(document));
        //获取今日的开始和结束时间
        Long startTime = DateUtil.getInstance().todayStartTime().getTime();
        Long endTime = DateUtil.getInstance().todayEndTime().getTime();
        homeWebsiteDto.setTodayDocumentNum(documentDao.selectCountByExample(new Example.Builder(Document.class)
                .where(WeekendSqls.<Document>custom().andEqualTo(Document::getStatus, EntitySetting.COMMON_SUCCESS)
                        .andBetween(Document::getCreatedAt, startTime, endTime)
                )
                .build()));
        User user = new User();
        user.setStatus(EntitySetting.COMMON_SUCCESS);
        homeWebsiteDto.setUserNum(userDao.selectCount(user));
        homeWebsiteDto.setTodayUserNum(userDao.selectCountByExample(new Example.Builder(User.class)
                .where(WeekendSqls.<User>custom().andEqualTo(User::getStatus, EntitySetting.COMMON_SUCCESS)
                        .andBetween(User::getRegisterTime, startTime, endTime)
                )
                .build()));
        dto.setDetail(homeWebsiteDto);
        return dto;
    }

    public ListDto<HomeRecommendDto> homeRecommend(Long userId) {
        ListDto<HomeRecommendDto> dto = new ListDto<>(true);
        Long time = System.currentTimeMillis();
        //判断当前时间是上半年还是下半年；当前时间比9月1号以前的小，就是上半年；
        Calendar day = Calendar.getInstance();
        day.set(Calendar.MONTH, 8);
        day.set(Calendar.DAY_OF_MONTH, 0);
        day.set(Calendar.HOUR_OF_DAY, 0);
        day.set(Calendar.MINUTE, 0);
        day.set(Calendar.SECOND, 0);
        day.set(Calendar.MILLISECOND, 0);
        Boolean upHalf = false;//true为上半年，下学期
        if (time < day.getTime().getTime()) {
            upHalf = true;
        }
        Integer grade = null;//年级
        Long subjectId = null;//学科
        Long provinceId = null;//地区
        if (userId != null && userId != 0L) {//有用户信息,查询用户信息
            User user = userDao.selectByPrimaryKey(userId);
            if (user != null) {
                grade = user.getGrade();
                subjectId = user.getSubjectId();
                provinceId = user.getProvinceId();
            } else {
                userId = null;
            }
        }
        List<Document> documents = documentDao.selectByHomeRecommend(grade, subjectId, provinceId, userId, upHalf);
        if (Collections3.isEmpty(documents) || documents.size() < 5) {//不足5份,去除条件再次查询
            List<Document> documents1 = documentDao.selectByHomeRecommend(grade, subjectId, null, userId, upHalf);
            if (documents.size() + documents1.size() < 5) {
                documents1.addAll(documentDao.selectByHomeRecommend(null, null, null, null, null));
            }
            for (int i = 0; i < 5 - documents.size(); i++) {
                documents.add(documents1.get(i));
            }
        }
        List<HomeRecommendDto> list = Lists.newArrayList();
        documents.forEach(d -> {
            list.add(BeanMapper.map(d, HomeRecommendDto.class));
        });
        dto.setList(list);
        return dto;
    }
}