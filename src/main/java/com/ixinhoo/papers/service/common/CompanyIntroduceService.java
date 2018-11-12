package com.ixinhoo.papers.service.common;

import com.chunecai.crumbs.api.client.StatusDto;
import com.chunecai.crumbs.api.dao.BaseDao;
import com.chunecai.crumbs.api.service.BaseService;
import com.ixinhoo.papers.dao.common.CompanyIntroduceDao;
import com.ixinhoo.papers.entity.common.CompanyIntroduce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CompanyIntroduceService extends BaseService<CompanyIntroduce> {
    @Autowired
    private CompanyIntroduceDao dao;

    @Override
    protected BaseDao getBaseDao() {
        return dao;
    }

    @Transactional
    public StatusDto saveCompanyIntroduce(CompanyIntroduce entity) {
        StatusDto dto = new StatusDto(false);
        if (entity == null) {
            dto.setMsg("实体为空");
        } else {
            if (entity.getId() == null || entity.getId() == 0L) {
                super.create(entity);
            } else {
                super.updateById(entity);
            }
            dto.setStatus(true);
        }
        return dto;
    }
}