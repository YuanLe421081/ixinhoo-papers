package com.ixinhoo.papers.rest.user;


import com.chunecai.crumbs.api.client.ListDto;
import com.chunecai.crumbs.api.client.PageDto;
import com.chunecai.crumbs.api.client.StatusDto;
import com.ixinhoo.papers.dto.user.UserOrderListDto;
import com.ixinhoo.papers.dto.user.UserOrderPreNumStatusDto;
import com.ixinhoo.papers.dto.user.UserOrderReqDto;
import com.ixinhoo.papers.entity.user.UserOrder;
import com.ixinhoo.papers.service.user.UserOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/user-order")
public class UserOrderRestController {

    @Autowired
    private UserOrderService service;


    @RequestMapping(value = "list", method = RequestMethod.POST)
    public PageDto<UserOrderListDto> list(UserOrderReqDto reqDto) {
        return service.pageUserOrderByType(reqDto);
    }

    /**
     * 根据预支付订单号查询订单状态
     *
     * @param preNum
     * @return
     */
    @RequestMapping(value = "status", method = RequestMethod.POST)
    public StatusDto status(String preNum) {
        return service.findUserOrderStatusByPreNum(preNum);
    }


    /**
     * 根据预支付订单号查询订单状态;
     *
     * @param list
     * @return
     */
    @RequestMapping(value = "status-list", method = RequestMethod.POST)
    public ListDto<UserOrderPreNumStatusDto> statusList(@RequestParam List<String> list) {
        return service.findUserOrderStatusByPreNumList(list);
    }

    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public StatusDto delete(@RequestParam("ids") List<Long> ids) {
        service.deleteByIds(ids);
        return new StatusDto(true);
    }

}