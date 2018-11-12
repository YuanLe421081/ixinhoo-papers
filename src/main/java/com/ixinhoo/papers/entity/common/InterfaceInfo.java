package com.ixinhoo.papers.entity.common;

import com.chunecai.crumbs.api.entity.AuditEntity;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * 调用21世纪接口信息
 *
 * @author cici
 */
@Table(name = "interface_info")
public class InterfaceInfo extends AuditEntity {
    @Column
    private Long time;//请求时间
    @Column
    private String reqData;//请求数据
    @Column
    private String rspData;//响应数据
    @Column
    private Integer type;//接口类型：地区、章节、资源

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getReqData() {
        return reqData;
    }

    public void setReqData(String reqData) {
        this.reqData = reqData;
    }

    public String getRspData() {
        return rspData;
    }

    public void setRspData(String rspData) {
        this.rspData = rspData;
    }
}