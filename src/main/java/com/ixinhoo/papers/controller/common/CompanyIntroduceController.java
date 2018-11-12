package com.ixinhoo.papers.controller.common;


import com.chunecai.crumbs.api.client.DetailDto;
import com.chunecai.crumbs.api.client.StatusDto;
import com.ixinhoo.papers.entity.common.CompanyIntroduce;
import com.ixinhoo.papers.service.common.CompanyIntroduceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/bg/company-introduce")
public class CompanyIntroduceController {

    @Autowired
    private CompanyIntroduceService service;


    @RequestMapping(value = "save", method = RequestMethod.POST)
    public StatusDto save(CompanyIntroduce entity) {
        return service.saveCompanyIntroduce(entity);
    }


    @RequestMapping(value = "show/{id}", method = RequestMethod.GET)
    public DetailDto<CompanyIntroduce> findById(@PathVariable("id") Long id) {
        return new DetailDto<>(true, service.findById(id));
    }

    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public StatusDto delete(@RequestParam("ids") List<Long> ids) {
        service.deleteByIds(ids);
        return new StatusDto(true);
    }


}