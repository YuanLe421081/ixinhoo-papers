package com.ixinhoo.papers.service.user;

import com.chunecai.crumbs.api.dao.BaseDao;
import com.chunecai.crumbs.api.service.BaseService;
import com.ixinhoo.papers.dao.user.UserLoginRecordDao;
import com.ixinhoo.papers.entity.user.UserLoginRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserLoginRecordService extends BaseService<UserLoginRecord> {
    @Autowired
    private UserLoginRecordDao dao;

    @Override
    protected BaseDao getBaseDao() {
        return dao;
    }

}