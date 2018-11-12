package com.ixinhoo.papers.rest.base;


import com.chunecai.crumbs.api.client.ListDto;
import com.ixinhoo.papers.dto.base.SchoolReqDto;
import com.ixinhoo.papers.entity.base.School;
import com.ixinhoo.papers.service.base.SchoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/school")
public class SchoolRestController {

    @Autowired
    private SchoolService service;


    @RequestMapping(value = "province", method = RequestMethod.POST)
    public ListDto<School> findByProvinceId(SchoolReqDto reqDto) {
        return new ListDto<>(true, service.findByProvinceId(reqDto));
    }

}