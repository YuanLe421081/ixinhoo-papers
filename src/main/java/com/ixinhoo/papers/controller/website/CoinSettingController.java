package com.ixinhoo.papers.controller.website;


import com.chunecai.crumbs.api.client.DetailDto;
import com.chunecai.crumbs.api.client.StatusDto;
import com.chunecai.crumbs.api.code.DataTable;
import com.chunecai.crumbs.api.util.DataTableUtil;
import com.ixinhoo.papers.entity.EntitySetting;
import com.ixinhoo.papers.entity.website.CoinSetting;
import com.ixinhoo.papers.service.website.CoinSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/api/bg/coin")
public class CoinSettingController {

    @Autowired
    private CoinSettingService service;

    @RequestMapping(value = "save", method = RequestMethod.POST)
    public StatusDto save(CoinSetting entity) {
        entity.setStatus(EntitySetting.COMMON_SUCCESS);
        service.save(entity);
        return new StatusDto(true);
    }


    @RequestMapping(value = "show/{id}", method = RequestMethod.GET)
    public DetailDto<CoinSetting> findById(@PathVariable("id") Long id) {
        return new DetailDto<>(true, service.findById(id));
    }


    @RequestMapping(value = "list", method = RequestMethod.POST)
    public DataTable<CoinSetting> list(HttpServletRequest request) {
        return service.listDatatable(DataTableUtil.toDataTable(request));
    }


}