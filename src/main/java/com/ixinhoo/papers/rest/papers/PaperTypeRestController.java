package com.ixinhoo.papers.rest.papers;


import com.chunecai.crumbs.api.client.ListDto;
import com.ixinhoo.papers.entity.papers.PaperType;
import com.ixinhoo.papers.service.papers.PaperTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/anon/paper-type")
public class PaperTypeRestController {

    @Autowired
    private PaperTypeService service;


    @RequestMapping(value = "list", method = RequestMethod.GET)
    public ListDto<PaperType> list() {
        return new ListDto<>(true, service.findAll());
    }


}