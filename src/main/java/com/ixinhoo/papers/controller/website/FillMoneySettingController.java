package com.ixinhoo.papers.controller.website;


import com.chunecai.crumbs.api.client.DetailDto;
import com.chunecai.crumbs.api.client.StatusDto;
import com.chunecai.crumbs.api.code.DataTable;
import com.chunecai.crumbs.api.util.DataTableUtil;
import com.ixinhoo.papers.entity.EntitySetting;
import com.ixinhoo.papers.entity.website.FillMoneySetting;
import com.ixinhoo.papers.service.website.FillMoneySettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(value = "/api/bg/fill-money")
public class FillMoneySettingController {

    @Autowired
    private FillMoneySettingService service;

    @RequestMapping(value = "save", method = RequestMethod.POST)
    public StatusDto save(FillMoneySetting entity) {
        entity.setStatus(EntitySetting.COMMON_SUCCESS);
        service.save(entity);
        return new StatusDto(true);
    }


    @RequestMapping(value = "show/{id}", method = RequestMethod.GET)
    public DetailDto<FillMoneySetting> findById(@PathVariable("id") Long id) {
        return new DetailDto<>(true, service.findById(id));
    }


    @RequestMapping(value = "list", method = RequestMethod.POST)
    public DataTable<FillMoneySetting> list(HttpServletRequest request) {
        return service.listDatatable(DataTableUtil.toDataTable(request));
    }

    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public StatusDto delete(@RequestParam("ids") List<Long> ids) {
        service.deleteByIds(ids);
        return new StatusDto(true);
    }


}