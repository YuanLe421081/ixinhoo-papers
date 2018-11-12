package com.ixinhoo.papers.entity.resources;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * 文档扩展表，id与文档一致
 * Created by Administrator on 2018/11/12.
 */
@Table(name="document_extend")
public class DocumentExtend {

	@Column(name="document_id")
	private Integer document_id;//资源id
	@Column(name="score_num")
	private Integer score_num;//资源评分次数
	@Column(name="score_sum")
	private Integer score_sum;//所有评分之和
	@Column(name="view_num")
	private Integer view_num;//资源浏览次数
	@Column(name="download_num")
	private Integer download_num;//资源下载次数
	@Column(name="revenue_coin")
	private Integer revenue_coin;//资源的备课币收益
	@Column(name="revenue_money")
	private Integer revenue_money;//资源的小额支付收益
	@Column(name="stage_id")
	private Integer stage_id;//学段id
	@Column(name="subject_id")
	private Integer subject_id;//学科id
	@Column(name="grade_id")
	private Integer grade_id;//年级id
	@Column(name="document_type")
	private Integer document_type;//资源的类型

	public Integer getDocument_id() { return document_id; }

	public void setDocument_id(Integer document_id) { this.document_id = document_id; }

	public Integer getScore_num() { return score_num; }

	public void setScore_num(Integer score_num) { this.score_num = score_num; }

	public Integer getScore_sum() { return score_sum; }

	public void setScore_sum(Integer score_sum) { this.score_sum = score_sum; }

	public Integer getView_num() { return view_num; }

	public void setView_num(Integer view_num) { this.view_num = view_num; }

	public Integer getDownload_num() { return download_num; }

	public void setDownload_num(Integer download_num) { this.download_num = download_num; }

	public Integer getRevenue_coin() { return revenue_coin; }

	public void setRevenue_coin(Integer revenue_coin) { this.revenue_coin = revenue_coin; }

	public Integer getRevenue_money() { return revenue_money; }

	public void setRevenue_money(Integer revenue_money) { this.revenue_money = revenue_money; }

	public Integer getStage_id() { return stage_id; }

	public void setStage_id(Integer stage_id) { this.stage_id = stage_id; }

	public Integer getSubject_id() { return subject_id; }

	public void setSubject_id(Integer subject_id) { this.subject_id = subject_id; }

	public Integer getGrade_id() { return grade_id; }

	public void setGrade_id(Integer grade_id) { this.grade_id = grade_id; }

	public Integer getDocument_type() { return document_type; }

	public void setDocument_type(Integer document_type) { this.document_type = document_type; }
}
