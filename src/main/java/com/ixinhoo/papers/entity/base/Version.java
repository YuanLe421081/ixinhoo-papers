package com.ixinhoo.papers.entity.base;

import com.chunecai.crumbs.api.entity.AuditEntity;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * 版本
 * Created by Administrator on 2018/11/12.
 */
@Table(name="version")
public class Version extends AuditEntity{

	@Column(name="versioin_id")
	private Integer version_id;
	@Column(name="version_name")
	private String version_name;
	@Column(name="stage_id")
	private Integer stage_id;
	@Column(name="subject_id")
	private Integer subject_id;
	@Column(name="path")
	private String path;
	@Column(name="zone_type")
	private Integer zone_type;
	@Column(name="status")
	private Integer status;

	public Integer getVersion_id() { return version_id; }

	public void setVersion_id(Integer version_id) { this.version_id = version_id; }

	public String getVersion_name() { return version_name; }

	public void setVersion_name(String version_name) { this.version_name = version_name; }

	public Integer getStage_id() { return stage_id; }

	public void setStage_id(Integer stage_id) { this.stage_id = stage_id; }

	public Integer getSubject_id() { return subject_id; }

	public void setSubject_id(Integer subject_id) { this.subject_id = subject_id; }

	public String getPath() { return path; }

	public void setPath(String path) { this.path = path; }

	public Integer getZone_type() { return zone_type; }

	public void setZone_type(Integer zone_type) { this.zone_type = zone_type; }

	public Integer getStatus() { return status; }

	public void setStatus(Integer status) { this.status = status; }

}
