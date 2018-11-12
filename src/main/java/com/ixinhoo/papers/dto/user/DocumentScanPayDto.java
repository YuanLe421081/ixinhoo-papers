package com.ixinhoo.papers.dto.user;

import com.ixinhoo.papers.dto.user.paper.PackagePaperScanPayDto;

/**
 * 资源下载
 * @author 448778074@qq.com (cici)
 */
public class DocumentScanPayDto extends PackagePaperScanPayDto {
    private String downloadUrl;//资源下载地址

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }
}
