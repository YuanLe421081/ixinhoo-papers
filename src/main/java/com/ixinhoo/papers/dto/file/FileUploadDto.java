package com.ixinhoo.papers.dto.file;


import com.chunecai.crumbs.api.client.StatusDto;

/**
 * 文件上传Vo,用于后台的文件上传实体
 *
 * @author 448778074@qq.com (cici)
 */
public class FileUploadDto extends StatusDto {
    private Long size;//上传的文件大小,
    private String basePath;//文件的最开始的基本路径
    private String ext;//文件的扩展名
    private String address;//文件的地址
    private String name;//文件的名称


    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
