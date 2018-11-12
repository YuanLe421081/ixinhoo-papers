package com.ixinhoo.papers.service.user;

import com.chunecai.crumbs.api.dao.BaseDao;
import com.chunecai.crumbs.api.service.BaseService;
import com.ixinhoo.papers.dao.user.UserMessageReadDao;
import com.ixinhoo.papers.entity.user.UserMessageRead;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.weekend.WeekendSqls;

import java.util.List;

@Service
public class UserMessageReadService extends BaseService<UserMessageRead> {
    @Autowired
    private UserMessageReadDao dao;

    @Override
    protected BaseDao getBaseDao() {
        return dao;
    }

    public List<UserMessageRead> findByUserIdAndStatus(Long userId, Integer status) {
        return dao.selectByExample(new Example.Builder(UserMessageRead.class)
                .where(WeekendSqls.<UserMessageRead>custom()
                        .andEqualTo(UserMessageRead::getStatus, status)
                        .andEqualTo(UserMessageRead::getUserId, userId)).build());
    }

    @Transactional
    public void deleteByUserId(Long userId) {
        dao.deleteByExample(new Example.Builder(UserMessageRead.class)
                .where(WeekendSqls.<UserMessageRead>custom()
                        .andEqualTo(UserMessageRead::getUserId, userId)).build());
    }
}