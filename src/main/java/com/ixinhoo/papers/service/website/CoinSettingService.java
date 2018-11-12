package com.ixinhoo.papers.service.website;

import com.chunecai.crumbs.api.dao.BaseDao;
import com.chunecai.crumbs.api.service.BaseService;
import com.ixinhoo.papers.dao.website.CoinSettingDao;
import com.ixinhoo.papers.entity.EntitySetting;
import com.ixinhoo.papers.entity.website.CoinSetting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.weekend.WeekendSqls;

@Service
public class CoinSettingService extends BaseService<CoinSetting> {
    @Autowired
    private CoinSettingDao dao;

    @Override
    protected BaseDao getBaseDao() {
        return dao;
    }

    /**
     * 查询激活的有效的设置信息
     *
     * @return
     */
    public CoinSetting findActiveCoinSetting() {
        return dao.selectOneByExample(new Example.Builder(CoinSetting.class)
                .where(WeekendSqls.<CoinSetting>custom()
                        .andEqualTo(CoinSetting::getStatus, EntitySetting.COMMON_SUCCESS)
                ).build());
    }
}