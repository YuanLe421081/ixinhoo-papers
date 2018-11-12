package com.ixinhoo.papers.service.file;

import com.chunecai.crumbs.api.client.DetailDto;
import com.chunecai.crumbs.code.util.date.DateUtil;
import com.chunecai.crumbs.code.util.key.UuidMaker;
import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.ixinhoo.papers.dto.file.FileUploadDto;
import com.ixinhoo.papers.entity.EntitySetting;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.storage.model.FetchRet;
import com.qiniu.util.Auth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Component
public class QiniuFileService {
    private Logger logger = LoggerFactory.getLogger(QiniuFileService.class);

    @Value("${qiniu.accessKey}")
    private String accessKey;
    @Value("${qiniu.secretKey}")
    private String secretKey;
    @Value("${qiniu.bucket}")
    private String bucket;
    @Value("${qiniu.bucketPrivate}")
    private String bucketPrivate;
    @Value("${qiniu.filePrivateUrl}")
    private String filePrivateUrl;

    /**
     * 文件的上传；
     *
     * @param file
     * @return
     * @throws IOException
     */
    public FileUploadDto uploadFile(MultipartFile file) throws IOException {
        FileUploadDto vo = new FileUploadDto();
        vo.setStatus(false);
        try {
            if (file != null) {
                //构造一个带指定Zone对象的配置类
                Configuration cfg = new Configuration(Zone.zone0());
                //...其他参数参考类注释
                UploadManager uploadManager = new UploadManager(cfg);
                //...生成上传凭证，然后准备上传
                Auth auth = Auth.create(accessKey, secretKey);
                String upToken = auth.uploadToken(bucket);
                //默认不指定key的情况下，以文件内容的hash值作为文件名
                String fileExt = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1).toLowerCase();//扩展名
                String key = DateUtil.getInstance().formatDate(System.currentTimeMillis(), "yyyy/MM/dd/") + UuidMaker.getInstance().getUuid(true) + "." + fileExt;
                try {
                    try {
                        Response response = uploadManager.put(file.getInputStream(), key, upToken, null, null);
                        //解析上传成功的结果
                        DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                        String encodedFileName = URLEncoder.encode(putRet.key, "utf-8");
                        String finalUrl = String.format("%s", encodedFileName);
                        vo.setAddress(finalUrl);
                        vo.setCode(putRet.key);
                    } catch (QiniuException ex) {
                        Response r = ex.response;
                        logger.error("七牛云上传文件异常:{}", r.toString());
                    }
                } catch (UnsupportedEncodingException ex) {
                    //ignore
                }
                vo.setSize(file.getSize());
                vo.setName(file.getOriginalFilename());
                vo.setStatus(true);
                vo.setExt(fileExt);
            }
        } catch (Exception e) {
            logger.error("文件上传异常:{}", e);
            vo.setStatus(false);
            vo.setMsg("文件上传异常");
            vo.setCode("exception");
        }
        return vo;
    }


    public FileUploadDto uploadFileToPrivate(MultipartFile file) {
        FileUploadDto vo = new FileUploadDto();
        vo.setStatus(false);
        try {
            if (file != null) {
                //构造一个带指定Zone对象的配置类
                Configuration cfg = new Configuration(Zone.zone0());
                //...其他参数参考类注释
                UploadManager uploadManager = new UploadManager(cfg);
                //...生成上传凭证，然后准备上传
                Auth auth = Auth.create(accessKey, secretKey);
                String upToken = auth.uploadToken(bucketPrivate);
                //默认不指定key的情况下，以文件内容的hash值作为文件名
                String fileExt = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1).toLowerCase();//扩展名
                String key = DateUtil.getInstance().formatDate(System.currentTimeMillis(), "yyyy/MM/dd/") + UuidMaker.getInstance().getUuid(true) + "." + fileExt;
                try {
                    try {
                        Response response = uploadManager.put(file.getInputStream(), key, upToken, null, null);
                        //解析上传成功的结果
                        DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                        String encodedFileName = URLEncoder.encode(putRet.key, "UTF-8");
                        String finalUrl = String.format("%s/%s", filePrivateUrl, encodedFileName);
                        long expireInSeconds = EntitySetting.QINIU_FILE_EXPIRE_SECONDS;//1小时，可以自定义链接过期时间
                        finalUrl = auth.privateDownloadUrl(finalUrl, expireInSeconds);
                        vo.setAddress(finalUrl);
                        vo.setCode(putRet.key);
                    } catch (QiniuException ex) {
                        Response r = ex.response;
                        logger.error("七牛云上传文件异常:{}", r.toString());
                    }
                } catch (UnsupportedEncodingException ex) {
                    //ignore
                }
                vo.setSize(file.getSize());
                vo.setName(file.getOriginalFilename());
                vo.setStatus(true);
                vo.setExt(fileExt);
            }
        } catch (Exception e) {
            logger.error("文件上传异常:{}", e);
            vo.setStatus(false);
            vo.setMsg("文件上传异常");
            vo.setCode("exception");
        }
        return vo;
    }

    /**
     * 通过url上传文件至公共存储空间或者私有存储空间
     *
     * @param url
     * @return
     */
    public FileUploadDto uploadFileByUrl(String url, Integer stage, boolean isPrivate) {
        FileUploadDto vo = new FileUploadDto();
        vo.setStatus(false);
        try {
            if (!Strings.isNullOrEmpty(url)) {
                //构造一个带指定Zone对象的配置类
                Configuration cfg = new Configuration(Zone.zone0());
                //...其他参数参考类注释

                //...生成上传凭证，然后准备上传
                Auth auth = Auth.create(accessKey, secretKey);
                BucketManager bucketManager = new BucketManager(auth, cfg);
                //默认不指定key的情况下，以文件内容的hash值作为文件名
                String fileExt;
                if(url.indexOf("?")!=-1){
                     fileExt = url.substring(0,url.lastIndexOf("?"));//去掉问号后面的信息
                }else{
                    fileExt = url;
                }
                fileExt = fileExt.substring(fileExt.lastIndexOf(".") + 1).toLowerCase();//扩展名
                String key = (stage != null ? stage + "/" : "") + DateUtil.getInstance().formatDate(System.currentTimeMillis(), "yyyy/MM/dd/") + UuidMaker.getInstance().getUuid(true) + "." + fileExt;
                //抓取网络资源到空间
                try {
                    FetchRet fetchRet;
                    if (isPrivate) {
                        fetchRet = bucketManager.fetch(url, bucketPrivate, key);
                    } else {
                        fetchRet = bucketManager.fetch(url, bucket, key);
                    }
                    vo.setSize(fetchRet.fsize);
                    vo.setCode(fetchRet.key);
                    vo.setName(key);
                    //因为是上传到私有仓库,存储key值,后面通过获取授权的URL进行下载
                    vo.setAddress(fetchRet.key);
                } catch (QiniuException ex) {
                    logger.error("七牛云抓取数据异常:{}", ex.response.toString());
                }
                vo.setStatus(true);
                vo.setExt(fileExt);
            }
        } catch (Exception e) {
            logger.error("文件上传异常:{}", e);
            vo.setStatus(false);
            vo.setMsg("文件上传异常");
            vo.setCode("exception");
        }
        return vo;
    }

    public DetailDto<String> getPrivateUrlByKey(String key) {
        DetailDto<String> dto = new DetailDto<>(false);
        if (Strings.isNullOrEmpty(key)) {
            dto.setMsg("传输的key值为空");
        } else {
            Auth auth = Auth.create(accessKey, secretKey);
            String encodedFileName = null;
            try {
                encodedFileName = URLEncoder.encode(key, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                //
            }
            String finalUrl = String.format("%s/%s", filePrivateUrl, encodedFileName);
            long expireInSeconds = EntitySetting.QINIU_FILE_EXPIRE_SECONDS;//1小时，可以自定义链接过期时间
            finalUrl = auth.privateDownloadUrl(finalUrl, expireInSeconds);
            dto.setDetail(finalUrl);
            dto.setStatus(true);
        }
        return dto;
    }
    public static void main(String[] args){
//        new QiniuFileService().uploadFileByUrl("http://yddown.21cnjy.com/NSoftMoved/2018/07/11/21cnjycom20180711183536_3_1863664_web.zip?st=D1FiRP8oBqYNYdYoPrjb8w&e=1532703150&n=%E8%AF%AD%E6%96%87%E5%85%AB%E5%B9%B4%E7%BA%A7%E4%B8%8A%E6%B2%AA%E6%95%99%E7%89%883.9%E3%80%8A%E6%B8%B8%E5%9B%AD%E4%B8%8D%E5%80%BC%E3%80%8B%E8%AF%BE%E4%BB%B6",1,true);
//        String url = "http://yddown.21cnjy.com/NSoftMoved/2018/07/11/21cnjycom20180711183536_3_1863664_web.zip?st=D1FiRP8oBqYNYdYoPrjb8w&e=1532703150&n=%E8%AF%AD%E6%96%87%E5%85%AB%E5%B9%B4%E7%BA%A7%E4%B8%8A%E6%B2%AA%E6%95%99%E7%89%883.9%E3%80%8A%E6%B8%B8%E5%9B%AD%E4%B8%8D%E5%80%BC%E3%80%8B%E8%AF%BE%E4%BB%B6";
        String url = "http://yddown.21cnjy.com/NSoftMoved/2018/07/11/21cnjycom20180711183536_3_1863664_web.zip k";//'、
        String fileExt = url.substring(0,url.lastIndexOf("?"));//去掉问号后面的信息
        System.out.println(fileExt);
    }
}
