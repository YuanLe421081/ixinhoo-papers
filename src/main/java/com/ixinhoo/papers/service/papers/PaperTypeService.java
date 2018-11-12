package com.ixinhoo.papers.service.papers;

import com.chunecai.crumbs.api.client.StatusDto;
import com.chunecai.crumbs.api.dao.BaseDao;
import com.chunecai.crumbs.api.service.BaseService;
import com.chunecai.crumbs.api.shiro.ShiroUser;
import com.chunecai.crumbs.api.util.UsersUtil;
import com.chunecai.crumbs.code.util.collection.Collections3;
import com.chunecai.crumbs.code.util.mapper.JsonMapper;
import com.cnjy21.api.service.APIPaperService;
import com.google.common.collect.Lists;
import com.ixinhoo.papers.dao.common.InterfaceInfoDao;
import com.ixinhoo.papers.dao.papers.PaperTypeDao;
import com.ixinhoo.papers.entity.EntitySetting;
import com.ixinhoo.papers.entity.common.InterfaceInfo;
import com.ixinhoo.papers.entity.papers.PaperType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PaperTypeService extends BaseService<PaperType> {
    @Autowired
    private PaperTypeDao dao;
    @Autowired
    private InterfaceInfoDao interfaceInfoDao;

    @Override
    protected BaseDao getBaseDao() {
        return dao;
    }

    /**
     * 清空同步--id与21一致
     *
     * @return
     */
    @Transactional
    public StatusDto syncPaperType() {
        StatusDto dto = new StatusDto(true);
        // 获取省份地区信息
        APIPaperService service = new APIPaperService();
        List<com.cnjy21.api.model.papers.PaperType> paperTypes = service.getPaperTypes();
        if (Collections3.isNotEmpty(paperTypes)) {
            InterfaceInfo interfaceInfo = new InterfaceInfo();
            interfaceInfo.setTime(System.currentTimeMillis());
            interfaceInfo.setType(EntitySetting.INTERFACE_PAPER_TYPE);
            interfaceInfo.setRspData(JsonMapper.nonDefaultMapper().toJson(paperTypes));
            ShiroUser user = UsersUtil.ShiroUser();
            if (user != null) {
                interfaceInfo.setCreateId(user.getId());
                interfaceInfo.setUpdateId(user.getId());
            }
            interfaceInfo.setCreateTime(System.currentTimeMillis());
            interfaceInfo.setUpdateTime(System.currentTimeMillis());
            interfaceInfoDao.insert(interfaceInfo);
            //清空重新添加
            dao.delete(null);
            List<PaperType> list = Lists.newArrayList();
            paperTypes.forEach(d -> {
                PaperType temp = new PaperType();
                if (d.getTypeId() != null) {
                    temp.setCode(d.getTypeId().toString());
                    temp.setId(d.getTypeId().longValue());
                }
                temp.setName(d.getTypeName());
                if (user != null) {
                    temp.setCreateId(user.getId());
                    temp.setUpdateId(user.getId());
                }
                temp.setCreateTime(System.currentTimeMillis());
                temp.setUpdateTime(System.currentTimeMillis());
                list.add(temp);
            });
            dao.insertList(list);
        }
        return dto;
    }


    public List<PaperType> findAll() {
        return dao.selectAll();
    }
}