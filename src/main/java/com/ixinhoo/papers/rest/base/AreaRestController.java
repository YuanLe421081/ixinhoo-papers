package com.ixinhoo.papers.rest.base;


import com.chunecai.crumbs.api.client.ListDto;
import com.ixinhoo.papers.entity.base.Area;
import com.ixinhoo.papers.service.base.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/area")
public class AreaRestController {

    @Autowired
    private AreaService service;


    @RequestMapping(value = "province", method = RequestMethod.GET)
    public ListDto<Area> findProvince() {
        return service.findProvince();
    }

}