package com.ixinhoo.papers.service.authority;

import com.chunecai.crumbs.api.dao.BaseDao;
import com.chunecai.crumbs.api.service.BaseService;
import com.ixinhoo.papers.dao.authority.DocumentAuditRecordDao;
import com.ixinhoo.papers.entity.authority.DocumentAuditRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DocumentAuditRecordService extends BaseService<DocumentAuditRecord> {
    @Autowired
    private DocumentAuditRecordDao dao;

    @Override
    protected BaseDao getBaseDao() {
        return dao;
    }

}