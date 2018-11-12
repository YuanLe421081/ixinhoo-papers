package com.ixinhoo.papers.rest.resources;


import com.chunecai.crumbs.api.client.DetailDto;
import com.chunecai.crumbs.api.client.ListDto;
import com.chunecai.crumbs.api.client.PageDto;
import com.ixinhoo.papers.dto.resources.DocumentDetailDto;
import com.ixinhoo.papers.dto.resources.DocumentSearchReqDto;
import com.ixinhoo.papers.dto.resources.DocumentSearchRspDto;
import com.ixinhoo.papers.service.resources.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/document")
public class DocumentRestController {

    @Autowired
    private DocumentService service;


    /**
     * 搜索
     *
     * @return
     */
    @RequestMapping(value = "search", method = RequestMethod.POST)
    public PageDto<DocumentSearchRspDto> search(DocumentSearchReqDto reqDto) {
        return service.searchDocument(reqDto);
    }


    /**
     * 资源详情
     *
     * @param id 资料id
     * @return
     */
    @RequestMapping(value = "detail", method = RequestMethod.POST)
    public DetailDto<DocumentDetailDto> detail(Long id, Integer stage) {
        return service.findDocumentDetailByIdAndStage(id, stage);
    }

    /**
     * 同类资料--预览资料详情页-右边推荐
     *
     * @param id 资料id
     * @return
     */
    @RequestMapping(value = "same-type", method = RequestMethod.POST)
    public ListDto<DocumentSearchRspDto> sameType(Long id) {
        return service.findSameTypeDocumentById(id);
    }

    /**
     * 猜你喜欢--预览资料详情页-底部推荐;
     * id==资料id
     *
     * @param id 资料id
     * @return
     */
    @RequestMapping(value = "guess", method = RequestMethod.POST)
    public ListDto<DocumentSearchRspDto> guess(Long id, Integer p, Integer s) {
        return service.findGuessDocumentByIdAndPage(id, p, s);
    }

    /**
     * 下载资料成功推荐(因为是批量下载)
     *
     * @param ids 资料id集合
     * @return
     */
    @RequestMapping(value = "download-recommend", method = RequestMethod.POST)
    public ListDto<DocumentSearchRspDto> downloadRecommend(List<Long> ids) {
        return service.findDownloadRecommendDocumentById(ids);
    }


}