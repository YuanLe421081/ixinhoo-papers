package com.ixinhoo.papers.rest.user;


import com.chunecai.crumbs.api.client.DetailDto;
import com.chunecai.crumbs.api.client.ListDto;
import com.chunecai.crumbs.api.client.StatusDto;
import com.ixinhoo.papers.dto.freemarker.AnswerCardDto;
import com.ixinhoo.papers.dto.user.paper.PackagePaperCardDto;
import com.ixinhoo.papers.dto.user.paper.PackagePaperSaveDto;
import com.ixinhoo.papers.dto.user.paper.PackagePaperScanPayDto;
import com.ixinhoo.papers.entity.user.PackagePaper;
import com.ixinhoo.papers.service.user.PackagePaperService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

/**
 * 手动组卷
 */
@RestController
@RequestMapping(value = "/api/v1/package-paper")
public class PackagePaperRestController {

    @Autowired
    private PackagePaperService service;

    /**
     * 保存用户试卷
     *
     * @return
     */
    @RequestMapping(value = "save", method = RequestMethod.POST)
    public DetailDto<Long> save(@RequestBody PackagePaperSaveDto reqDto) {
        return service.savePackagePaper(reqDto);
    }

    /**
     * 检查用户是否有下载权限；备课币是否足够；
     * 不足够返回微信&支付宝的二维码信息
     *
     * @return
     */
    @RequestMapping(value = "check", method = RequestMethod.POST)
    public DetailDto<PackagePaperScanPayDto> check(Long userId, Long id) {
        return service.checkDownloadPaper(userId, id);
    }

    /**
     * 下载答题卡
     *
     * @return
     */
    @RequestMapping(value = "card", method = RequestMethod.POST)
    public void card(PackagePaperCardDto reqDto, HttpServletResponse response) throws UnsupportedEncodingException {
        DetailDto<AnswerCardDto> detailDto = service.findCardByPackagePaper(reqDto);
        Configuration cfg = new Configuration();
        cfg.setDefaultEncoding("utf-8");
        cfg.setClassForTemplateLoading(this.getClass(),
                "/template/card");
        Template t = null;
        try {
            if (reqDto.getAnswerCard() == 1) {
                t = cfg.getTemplate("table.xml");
            } else if (reqDto.getAnswerCard() == 2) {
                t = cfg.getTemplate("standard.xml");
            } else {
                t = cfg.getTemplate("dense.xml");
            }
            t.setEncoding("utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 输出文档路径及名称
        response.setContentType("application/msword");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Disposition", "attachment;Filename=" + new String((reqDto.getPaperTitle() + ".doc").getBytes(), "iso-8859-1"));
        Writer out = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter(
                    response.getOutputStream(), "utf-8"));
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        try {
            t.process(detailDto.getDetail(), out);
            out.close();
        } catch (TemplateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 下载试卷；
     *
     * @param id 组卷记录id
     * @return
     */
    @RequestMapping(value = "download", method = RequestMethod.POST)
    public void download(Long userId, Long id, HttpServletResponse response) {
        service.downloadPaperByPackageId(userId, id, response);
    }

    /**
     * 我的组卷--组卷记录
     *
     * @return
     */
    @RequestMapping(value = "list", method = RequestMethod.POST)
    public ListDto<PackagePaper> list(Long userId) {
        return service.findByUserId(userId);
    }

    /**
     * 我的组卷--组卷记录--删除组卷记录
     *
     * @return
     */
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public StatusDto delete(@RequestParam List<Long> ids) {
        service.deleteByIds(ids);
        return new StatusDto(true);
    }

}