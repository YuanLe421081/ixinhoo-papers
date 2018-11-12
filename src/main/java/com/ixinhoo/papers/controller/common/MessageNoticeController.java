package com.ixinhoo.papers.controller.common;


import com.chunecai.crumbs.api.client.DetailDto;
import com.chunecai.crumbs.api.client.StatusDto;
import com.chunecai.crumbs.api.code.DataTable;
import com.chunecai.crumbs.api.util.DataTableUtil;
import com.ixinhoo.papers.entity.common.MessageNotice;
import com.ixinhoo.papers.service.common.MessageNoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(value = "/api/bg/message-notice")
public class MessageNoticeController {

    @Autowired
    private MessageNoticeService service;


    @RequestMapping(value = "save", method = RequestMethod.POST)
    public StatusDto save(MessageNotice entity) {
        service.save(entity);
        return new StatusDto(true);
    }


    @RequestMapping(value = "show/{id}", method = RequestMethod.GET)
    public DetailDto<MessageNotice> findById(@PathVariable("id") Long id) {
        return new DetailDto<>(true, service.findById(id));
    }

    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public StatusDto delete(@RequestParam("ids") List<Long> ids) {
        service.deleteByIds(ids);
        return new StatusDto(true);
    }

    @RequestMapping(value = "list", method = RequestMethod.POST)
    public DataTable<MessageNotice> list(HttpServletRequest request) {
        return service.listDatatable(DataTableUtil.toDataTable(request));
    }


}