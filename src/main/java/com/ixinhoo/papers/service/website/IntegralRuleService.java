package com.ixinhoo.papers.service.website;

import com.chunecai.crumbs.api.client.StatusDto;
import com.chunecai.crumbs.api.dao.BaseDao;
import com.chunecai.crumbs.api.service.BaseService;
import com.chunecai.crumbs.code.util.collection.Collections3;
import com.ixinhoo.papers.dao.website.IntegralRuleDao;
import com.ixinhoo.papers.entity.website.IntegralRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class IntegralRuleService extends BaseService<IntegralRule> {
    @Autowired
    private IntegralRuleDao dao;

    @Override
    protected BaseDao getBaseDao() {
        return dao;
    }

    public List<IntegralRule> listAll() {
        return dao.selectAll();
    }

    @Transactional
    public StatusDto saveAll(List<IntegralRule> list) {
        StatusDto dto = new StatusDto(false);
        if (Collections3.isEmpty(list)) {
            dto.setMsg("参数为空");
        } else {
            list.forEach(d -> {
                super.updateById(d);
            });
            dto.setStatus(true);
        }
        return dto;
    }
}