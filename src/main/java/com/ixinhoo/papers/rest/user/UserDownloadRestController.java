package com.ixinhoo.papers.rest.user;


import com.chunecai.crumbs.api.client.DetailDto;
import com.chunecai.crumbs.api.client.ListDto;
import com.chunecai.crumbs.api.client.PageDto;
import com.ixinhoo.papers.dto.user.DocumentScanPayDto;
import com.ixinhoo.papers.dto.user.UserDownloadDto;
import com.ixinhoo.papers.service.user.UserDownloadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 用户下载
 */
@RestController
@RequestMapping(value = "/api/v1/user-download")
public class UserDownloadRestController {

    @Autowired
    private UserDownloadService service;

    /**
     * 购物车--批量下载;文档； TODO cici--还有直接扫码支付？
     *
     * @return
     */
    @RequestMapping(value = "document", method = RequestMethod.POST)
    public ListDto<String> document(Long userId, @RequestParam List<Long> documentIds) {
        return service.createByUserAndDocument(userId, documentIds);
    }


    /**
     * 我的下载
     *
     * @return
     */
    @RequestMapping(value = "list", method = RequestMethod.POST)
    public PageDto<UserDownloadDto> list(Long userId, Integer p, Integer s) {
        return service.listDocumentByUserDownload(userId, p, s);
    }

    /**
     * 单个下载资源；
     * 检查用户是否有下载权限；备课币是否足够；
     * 不足够返回微信&支付宝的二维码信息
     *
     * @param id     资源id
     * @param userId 用户id
     * @return
     */
    @RequestMapping(value = "document-download", method = RequestMethod.POST)
    public DetailDto<DocumentScanPayDto> checkAndDownload(Long userId, Long id) {
        return service.checkDownloadResource(userId, id);
    }
}