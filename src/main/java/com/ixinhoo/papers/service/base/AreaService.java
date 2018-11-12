package com.ixinhoo.papers.service.base;

import com.chunecai.crumbs.api.client.ListDto;
import com.chunecai.crumbs.api.client.StatusDto;
import com.chunecai.crumbs.api.dao.BaseDao;
import com.chunecai.crumbs.api.service.BaseService;
import com.chunecai.crumbs.api.shiro.ShiroUser;
import com.chunecai.crumbs.api.util.UsersUtil;
import com.chunecai.crumbs.code.util.collection.Collections3;
import com.chunecai.crumbs.code.util.mapper.JsonMapper;
import com.cnjy21.api.model.common.Province;
import com.cnjy21.api.service.APIDocumentService;
import com.google.common.collect.Lists;
import com.ixinhoo.papers.dao.base.AreaDao;
import com.ixinhoo.papers.dao.common.InterfaceInfoDao;
import com.ixinhoo.papers.entity.EntitySetting;
import com.ixinhoo.papers.entity.base.Area;
import com.ixinhoo.papers.entity.common.InterfaceInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AreaService extends BaseService<Area> {
    @Autowired
    private AreaDao dao;
    @Autowired
    private InterfaceInfoDao interfaceInfoDao;

    @Override
    protected BaseDao getBaseDao() {
        return dao;
    }

    @Transactional
    public StatusDto syncArea() {
        StatusDto dto = new StatusDto(true);
        // 获取省份地区信息
        APIDocumentService service = new APIDocumentService();
        List<Province> provinces = service.getProvinces();
        if (Collections3.isNotEmpty(provinces)) {
            InterfaceInfo interfaceInfo = new InterfaceInfo();
            interfaceInfo.setTime(System.currentTimeMillis());
            interfaceInfo.setType(EntitySetting.INTERFACE_AREA);
            interfaceInfo.setRspData(JsonMapper.nonDefaultMapper().toJson(provinces));
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
            List<Area> list = Lists.newArrayList();
            provinces.forEach(d -> {
                Area area = new Area();
                if (d.getId() != null) {
                    area.setCode(d.getId().toString());
                    area.setId(d.getId().longValue());
                }
                area.setLevel(d.getLevel());
                area.setName(d.getName());
                area.setPinyin(d.getPinyin());
                area.setSupId(d.getUpid() == null ? 0L : d.getUpid().longValue());
                if (user != null) {
                    area.setCreateId(user.getId());
                    area.setUpdateId(user.getId());
                }
                area.setCreateTime(System.currentTimeMillis());
                area.setUpdateTime(System.currentTimeMillis());
                area.setSortName(d.getShortname());
                list.add(area);
            });
            dao.insertList(list);
        }
        return dto;
    }

    public ListDto<Area> findProvince() {
        ListDto<Area> dto = new ListDto<>();
        dto.setStatus(true);
        Area area = new Area();
        area.setLevel(1);
        dto.setList(dao.select(area));
        return dto;
    }
}