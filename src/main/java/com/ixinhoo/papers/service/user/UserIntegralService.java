package com.ixinhoo.papers.service.user;

import com.chunecai.crumbs.api.dao.BaseDao;
import com.chunecai.crumbs.api.service.BaseService;
import com.ixinhoo.papers.dao.user.UserIntegralDao;
import com.ixinhoo.papers.entity.user.UserIntegral;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserIntegralService extends BaseService<UserIntegral> {
    @Autowired
    private UserIntegralDao dao;


    @Override
    protected BaseDao getBaseDao() {
        return dao;
    }

}