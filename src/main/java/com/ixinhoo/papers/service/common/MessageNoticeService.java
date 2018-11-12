package com.ixinhoo.papers.service.common;

import com.chunecai.crumbs.api.dao.BaseDao;
import com.chunecai.crumbs.api.service.BaseService;
import com.ixinhoo.papers.dao.common.MessageNoticeDao;
import com.ixinhoo.papers.entity.common.MessageNotice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageNoticeService extends BaseService<MessageNotice> {
    @Autowired
    private MessageNoticeDao dao;


    @Override
    protected BaseDao getBaseDao() {
        return dao;
    }

}