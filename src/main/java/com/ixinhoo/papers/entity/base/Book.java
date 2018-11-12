package com.ixinhoo.papers.entity.base;

import com.chunecai.crumbs.api.entity.AuditEntity;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * 册别
 * Created by Administrator on 2018/11/9.
 */
@Table(name="book")
public class Book extends AuditEntity{
	@Column(name="book_id")
	private Integer bookId;//册别id
	@Column(name="book_name")
	private String bookName;//册别名字
	@Column(name="path")
	private String path;//册别的关系路径
	@Column(name="stage_id")
	private Integer stageId;//册别所属学段
	@Column(name="subject_id")
	private Integer subjectId;//册别所属学科
	@Column(name="grade_id")
	private Integer gradeId;//册别所属年级，0表示没有年级
	@Column(name="term")
	private Integer term;//册别所属学期，1表示上学期，2表示下学期，0表示没有学期
	@Column(name="book_version_id")
	private Integer bookVersionId;//教材版本id，如苏教版、人教版、沪教版等，同步资料必填
	@Column(name="version_id")
	private Integer versionId;//版本id，与version表关联
	@Column(name="zone_type")
	private Integer zoneType;//教材所属专区类型，0表示同步备课，其他表示某个专区
	@Column(name="status")
	private Integer status;//状态，是否有效：0:表示无效，1:表示有效

	public Integer getBookId() { return bookId; }

	public void setBookId(Integer bookId) { this.bookId = bookId; }

	public String getBookName() { return bookName; }

	public void setBookName(String bookName) { this.bookName = bookName; }

	public String getPath() { return path; }

	public void setPath(String path) { this.path = path; }

	public Integer getStageId() { return stageId; }

	public void setStageId(Integer stageId) { this.stageId = stageId; }

	public Integer getSubjectId() { return subjectId; }

	public void setSubjectId(Integer subjectId) { this.subjectId = subjectId; }

	public Integer getGradeId() { return gradeId; }

	public void setGradeId(Integer gradeId) { this.gradeId = gradeId; }

	public Integer getTerm() { return term; }

	public void setTerm(Integer term) { this.term = term; }

	public Integer getBookVersionId() { return bookVersionId; }

	public void setBookVersionId(Integer bookVersionId) { this.bookVersionId = bookVersionId; }

	public Integer getVersionId() { return versionId; }

	public void setVersionId(Integer versionId) { this.versionId = versionId; }

	public Integer getZoneType() { return zoneType; }

	public void setZoneType(Integer zoneType) { this.zoneType = zoneType; }

	public Integer getStatus() { return status; }

	public void setStatus(Integer status) { this.status = status; }
}
