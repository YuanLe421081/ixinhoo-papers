package com.ixinhoo.papers.entity.base;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * 学段学科
 * Created by Administrator on 2018/11/12.
 */
@Table(name="stage_subject")
public class StageSubject {

	@Column(name="id")
	private Integer id;//主键
	@Column(name="stage_id")
	private Integer stage_id;//学段
	@Column(name="stage_name")
	private String stage_name;//学段名称
	@Column(name="subject_id")
	private Integer subject_id;//学科
	@Column(name="subject_name")
	private String subject_name;//学科名称

	public Integer getId() { return id; }

	public void setId(Integer id) { this.id = id; }

	public Integer getStage_id() { return stage_id; }

	public void setStage_id(Integer stage_id) { this.stage_id = stage_id; }

	public String getStage_name() { return stage_name; }

	public void setStage_name(String stage_name) { this.stage_name = stage_name; }

	public Integer getSubject_id() { return subject_id; }

	public void setSubject_id(Integer subject_id) { this.subject_id = subject_id; }

	public String getSubject_name() { return subject_name; }

	public void setSubject_name(String subject_name) { this.subject_name = subject_name; }
}
