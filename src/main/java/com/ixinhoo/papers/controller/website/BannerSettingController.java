package com.ixinhoo.papers.controller.website;


import com.chunecai.crumbs.api.client.DetailDto;
import com.chunecai.crumbs.api.client.StatusDto;
import com.chunecai.crumbs.api.code.DataTable;
import com.chunecai.crumbs.api.util.DataTableUtil;
import com.ixinhoo.papers.dto.website.BannerRuleDto;
import com.ixinhoo.papers.dto.website.BannerRuleRspDto;
import com.ixinhoo.papers.dto.website.PromotionActivityDto;
import com.ixinhoo.papers.dto.website.PromotionActivityRspDto;
import com.ixinhoo.papers.entity.EntitySetting;
import com.ixinhoo.papers.entity.website.BannerRule;
import com.ixinhoo.papers.entity.website.BannerSetting;
import com.ixinhoo.papers.service.website.BannerSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(value = "/api/bg/banner")
public class BannerSettingController {

    @Autowired
    private BannerSettingService service;

    @RequestMapping(value = "save", method = RequestMethod.POST)
    public StatusDto save(BannerSetting entity) {
        entity.setStatus(EntitySetting.COMMON_SUCCESS);
        service.save(entity);
        return new StatusDto(true);
    }


    @RequestMapping(value = "show/{id}", method = RequestMethod.GET)
    public DetailDto<BannerSetting> findById(@PathVariable("id") Long id) {
        return new DetailDto<>(true, service.findById(id));
    }


    @RequestMapping(value = "list", method = RequestMethod.POST)
    public DataTable<BannerSetting> list(HttpServletRequest request) {
        return service.listDatatable(DataTableUtil.toDataTable(request));
    }

    @RequestMapping(value = "list-rule/{id}", method = RequestMethod.POST)
    public DataTable<BannerRule> listRule(@PathVariable("id") Long id, HttpServletRequest request) {
        return service.listBannerRuleDatatable(id,DataTableUtil.toDataTable(request));
    }

    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public StatusDto delete(@RequestParam("ids") List<Long> ids) {
        service.deleteByIds(ids);
        return new StatusDto(true);
    }

    @RequestMapping(value = "update-rule-status", method = RequestMethod.POST)
    public StatusDto updateRuleStatusById(@RequestParam("ids") List<Long> ids,@RequestParam("status") Integer status) {
        return service.updateRuleStatusById(ids,status);
    }

    @RequestMapping(value = "save-rule", method = RequestMethod.POST)
    public StatusDto saveRule(BannerRuleDto entity) {
        return service.saveBannerRule(entity);
    }

    @RequestMapping(value = "delete-rule", method = RequestMethod.POST)
    public StatusDto deleteRule(@RequestParam("ids") List<Long> ids) {
        return service.deleteBannerRuleByIds(ids);
    }

    @RequestMapping(value = "show-rule/{id}", method = RequestMethod.GET)
    public DetailDto<BannerRuleRspDto> findRuleById(@PathVariable("id") Long id) {
        return service.findBannerRuleById(id);
    }

}