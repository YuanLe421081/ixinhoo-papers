package com.ixinhoo.papers.service.authority;

import com.chunecai.crumbs.api.dao.BaseDao;
import com.chunecai.crumbs.api.service.BaseService;
import com.chunecai.crumbs.api.shiro.ShiroUser;
import com.chunecai.crumbs.api.util.UsersUtil;
import com.ixinhoo.papers.dao.authority.SystemUserOperateDao;
import com.ixinhoo.papers.entity.authority.SystemUserOperate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SystemUserOperateService extends BaseService<SystemUserOperate> {
    @Autowired
    private SystemUserOperateDao dao;


    @Override
    protected BaseDao getBaseDao() {
        return dao;
    }

    @Transactional(readOnly = false)
    public void loggerInsert(SystemUserOperate entity) {
        entity.setTime(System.currentTimeMillis());
        ShiroUser user = UsersUtil.ShiroUser();
        if (user != null) {
            entity.setCreateId(user.getId());
            entity.setUpdateId(user.getId());
        }
        entity.setCreateTime(System.currentTimeMillis());
        entity.setUpdateTime(System.currentTimeMillis());
        dao.insert(entity);
    }
}