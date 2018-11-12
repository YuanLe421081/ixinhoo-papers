package com.ixinhoo.papers.service.common;

import com.chunecai.crumbs.api.client.DetailDto;
import com.chunecai.crumbs.api.dao.BaseDao;
import com.chunecai.crumbs.api.service.BaseService;
import com.ixinhoo.papers.dao.common.UserFeedbackDao;
import com.ixinhoo.papers.dto.user.UserDto;
import com.ixinhoo.papers.entity.common.UserFeedback;
import com.ixinhoo.papers.entity.user.User;
import com.ixinhoo.papers.entity.user.UserExtend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserFeedbackService extends BaseService<UserFeedback> {
    @Autowired
    private UserFeedbackDao dao;

    @Override
    protected BaseDao getBaseDao() {
        return dao;
    }

}