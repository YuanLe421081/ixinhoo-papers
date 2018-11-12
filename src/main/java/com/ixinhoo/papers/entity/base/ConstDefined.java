package com.ixinhoo.papers.entity.base;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * 常量定义
 * Created by Administrator on 2018/11/12.
 */
@Table(name="const_defined")
public class ConstDefined {

	@Column(name="id")
	private Integer id;//主键
	@Column(name="const_type")
	private Integer const_type;//常量类型，对应const_type中的type
	@Column(name="const_value")
	private Integer const_valus;//常量具体的取值
	@Column(name="const_name")
	private String const_name;//常量的名称含义
	@Column(name="isvalid")
	private Integer isvalid;//记录是否有效

	public Integer getId() { return id; }

	public void setId(Integer id) { this.id = id; }

	public Integer getConst_type() { return const_type; }

	public void setConst_type(Integer const_type) { this.const_type = const_type; }

	public Integer getConst_valus() { return const_valus; }

	public void setConst_valus(Integer const_valus) { this.const_valus = const_valus; }

	public String getConst_name() { return const_name; }

	public void setConst_name(String const_name) { this.const_name = const_name; }

	public Integer getIsvalid() { return isvalid; }

	public void setIsvalid(Integer isvalid) { this.isvalid = isvalid; }

}
