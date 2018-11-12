package com.ixinhoo.papers.service.papers;

import com.chunecai.crumbs.api.dao.BaseDao;
import com.chunecai.crumbs.api.service.BaseService;
import com.ixinhoo.papers.dao.papers.SubjectQuestionTypeDao;
import com.ixinhoo.papers.entity.papers.SubjectQuestionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubjectQuestionTypeService extends BaseService<SubjectQuestionType> {
    @Autowired
    private SubjectQuestionTypeDao dao;

    @Override
    protected BaseDao getBaseDao() {
        return dao;
    }

}