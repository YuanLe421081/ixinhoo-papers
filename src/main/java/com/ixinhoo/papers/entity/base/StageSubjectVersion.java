package com.ixinhoo.papers.entity.base;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * 学段、学科和教材版本关系表
 * Created by Administrator on 2018/11/12.
 */
@Table(name="stage_subject_version")
public class StageSubjectVersion {

	@Column(name="id")
	private Integer id;//主键
	@Column(name="stage_id")
	private Integer stage_id;//学段
	@Column(name="subject_id")
	private Integer subject_id;//学科
	@Column(name="book_version_id")
	private Integer book_version_id;//教材版本id
	@Column(name="book_version_name")
	private Integer book_version_name;//教材版本名

	public Integer getId() { return id; }

	public void setId(Integer id) { this.id = id; }

	public Integer getStage_id() { return stage_id; }

	public void setStage_id(Integer stage_id) { this.stage_id = stage_id; }

	public Integer getSubject_id() { return subject_id; }

	public void setSubject_id(Integer subject_id) { this.subject_id = subject_id; }

	public Integer getBook_version_id() { return book_version_id; }

	public void setBook_version_id(Integer book_version_id) { this.book_version_id = book_version_id; }

	public Integer getBook_version_name() { return book_version_name; }

	public void setBook_version_name(Integer book_version_name) { this.book_version_name = book_version_name; }


}
