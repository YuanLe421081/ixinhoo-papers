package com.ixinhoo.papers.rest.user;


import com.chunecai.crumbs.api.client.DetailDto;
import com.chunecai.crumbs.api.client.ListDto;
import com.chunecai.crumbs.api.client.StatusDto;
import com.ixinhoo.papers.dto.user.ShoppingCartDocumentDto;
import com.ixinhoo.papers.service.user.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 购物车
 */
@RestController
@RequestMapping(value = "/api/v1/shopping-cart")
public class ShoppingCartRestController {

    @Autowired
    private ShoppingCartService service;

    /**
     * 加入购物车
     *
     * @return
     */
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public DetailDto<Integer> add(Long userId, Long documentId) {
        return service.createByUserAndDocument(userId, documentId);
    }


    /**
     * 购物车列表
     *
     * @return
     */
    @RequestMapping(value = "list", method = RequestMethod.POST)
    public ListDto<ShoppingCartDocumentDto> list(Long userId) {
        return service.listDocumentByShoppingCart(userId);
    }


    /**
     * 购物车总数
     *
     * @return
     */
    @RequestMapping(value = "count", method = RequestMethod.POST)
    public DetailDto<Integer> count(Long userId) {
        return new DetailDto<>(true, service.listDocumentByShoppingCart(userId).getList().size());
    }

    /**
     * 购物车列表--批量删除；购物车主键id集合
     *
     * @return
     */
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public StatusDto delete(@RequestParam List<Long> ids) {
        service.deleteByIds(ids);
        return new StatusDto(true);
    }


}