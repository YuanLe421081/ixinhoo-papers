package com.ixinhoo.papers.controller.website;


import com.chunecai.crumbs.api.client.DetailDto;
import com.chunecai.crumbs.api.client.StatusDto;
import com.chunecai.crumbs.api.code.DataTable;
import com.chunecai.crumbs.api.util.DataTableUtil;
import com.ixinhoo.papers.dto.website.PromotionActivityDto;
import com.ixinhoo.papers.dto.website.PromotionActivityRspDto;
import com.ixinhoo.papers.entity.website.PromotionActivity;
import com.ixinhoo.papers.service.website.PromotionActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 促销活动
 */
@RestController
@RequestMapping(value = "/api/bg/promotion-activity")
public class PromotionActivityController {

    @Autowired
    private PromotionActivityService service;

    @RequestMapping(value = "save", method = RequestMethod.POST)
    public StatusDto save(PromotionActivityDto entity) {
        return service.savePromotionActivity(entity);
    }


    @RequestMapping(value = "show/{id}", method = RequestMethod.GET)
    public DetailDto<PromotionActivityRspDto> findById(@PathVariable("id") Long id) {
        return service.findPromotionById(id);
    }


    @RequestMapping(value = "list", method = RequestMethod.POST)
    public DataTable<PromotionActivity> list(HttpServletRequest request) {
        return service.listDatatable(DataTableUtil.toDataTable(request));
    }

    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public StatusDto delete(@RequestParam("ids") List<Long> ids) {
        return service.deletePromotionByIds(ids);
    }

    @RequestMapping(value = "status", method = RequestMethod.POST)
    public StatusDto updateStatus(@RequestParam("ids") List<Long> ids, @RequestParam("status") Integer status) {
        return service.updateStatusByIds(ids, status);
    }


}