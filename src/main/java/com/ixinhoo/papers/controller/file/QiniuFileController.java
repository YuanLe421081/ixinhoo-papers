package com.ixinhoo.papers.controller.file;

import com.chunecai.crumbs.api.client.DetailDto;
import com.ixinhoo.papers.dto.file.FileUploadDto;
import com.ixinhoo.papers.service.file.QiniuFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 七牛云--文件存储;
 *
 * @author cici
 */
@Controller
@RequestMapping({"/api/bg/file"})
public class QiniuFileController {
    @Autowired
    private QiniuFileService service;


    /**
     * 文件的上传，接收name默认为file的值；上传至公开空间
     *
     * @param file
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "upload", method = {RequestMethod.POST})
    @ResponseBody
    public FileUploadDto uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        return service.uploadFile(file);
    }


    /**
     * 文件的上传，接收name默认为file的值;上传至私有空间
     *
     * @param file
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "upload-private", method = {RequestMethod.POST})
    @ResponseBody
    public FileUploadDto uploadFileToPrivate(@RequestParam("file") MultipartFile file) throws IOException {
        return service.uploadFileToPrivate(file);
    }


    /**
     * 根据传输的key获取私有空间的授权url
     *
     * @return
     */
    @RequestMapping(value = "url", method = {RequestMethod.POST})
    @ResponseBody
    public DetailDto<String> urlByKey(@RequestParam("key") String key) {
        return service.getPrivateUrlByKey(key);
    }


}
