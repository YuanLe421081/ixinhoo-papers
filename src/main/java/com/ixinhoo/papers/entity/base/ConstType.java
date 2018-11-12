package com.ixinhoo.papers.entity.base;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * 常量
 * Created by Administrator on 2018/11/12.
 */
@Table(name="const_type")
public class ConstType {

	@Column(name="id;")
	private Integer id;//主键
	@Column(name="type")
	private Integer type;//常量类型id
	@Column(name="type_name")
	private String type_name;//常量类型名称，如学段、学科、资料类型等
	@Column(name="isvalid")
	private Integer isvalid;//字段是否有效

	public Integer getId() { return id; }

	public void setId(Integer id) { this.id = id; }

	public Integer getType() { return type; }

	public void setType(Integer type) { this.type = type; }

	public String getType_name() { return type_name; }

	public void setType_name(String type_name) { this.type_name = type_name; }

	public Integer getIsvalid() { return isvalid; }

	public void setIsvalid(Integer isvalid) { this.isvalid = isvalid; }


}
