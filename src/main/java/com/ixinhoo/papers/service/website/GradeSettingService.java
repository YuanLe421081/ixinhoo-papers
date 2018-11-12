package com.ixinhoo.papers.service.website;

import com.chunecai.crumbs.api.client.StatusDto;
import com.chunecai.crumbs.api.dao.BaseDao;
import com.chunecai.crumbs.api.service.BaseService;
import com.chunecai.crumbs.code.util.collection.Collections3;
import com.ixinhoo.papers.dao.website.GradeSettingDao;
import com.ixinhoo.papers.entity.website.GradeSetting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GradeSettingService extends BaseService<GradeSetting> {
    @Autowired
    private GradeSettingDao dao;

    @Override
    protected BaseDao getBaseDao() {
        return dao;
    }

    public List<GradeSetting> listAll() {
        return dao.selectAll();
    }

    @Transactional
    public StatusDto saveAll(List<GradeSetting> settings) {
        StatusDto dto = new StatusDto(false);
        if (Collections3.isEmpty(settings)) {
            dto.setMsg("参数为空");
        } else {
            settings.stream().sorted((h1, h2) -> h2.getId().compareTo(h1.getId()));
            int length = settings.size();
            for (int i = 0; i < settings.size(); i++) {
                GradeSetting gradeSetting = settings.get(i);
                if (i < length - 1) {
                    gradeSetting.setEndIntegral(settings.get(i + 1).getBeginIntegral());
                } else {
                    gradeSetting.setEndIntegral(Integer.MAX_VALUE);
                }
                super.updateById(gradeSetting);
            }
            dto.setStatus(true);
        }
        return dto;
    }
}