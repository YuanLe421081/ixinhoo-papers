package com.ixinhoo.papers.entity.resources;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * 资源预览表
 * Created by Administrator on 2018/11/12.
 */
@Table(name="document_preview")
public class DocumentPreview {

	@Column(name="document_id")
	private Integer document_id;//主键
	@Column(name="document_code")
	private Integer document_code;//对应资源表的document_code
	@Column(name="preview_files")
	private String preview_files;//资源预览文件集
	@Column(name="file_type")
	private String file_type;//资源预览文件类型
	@Column(name="subsets")
	private String subsets;//资源预览文件子集

	public Integer getDocument_id() { return document_id; }

	public void setDocument_id(Integer document_id) { this.document_id = document_id; }

	public Integer getDocument_code() { return document_code; }

	public void setDocument_code(Integer document_code) { this.document_code = document_code; }

	public String getPreview_files() { return preview_files; }

	public void setPreview_files(String preview_files) { this.preview_files = preview_files; }

	public String getFile_type() { return file_type; }

	public void setFile_type(String file_type) { this.file_type = file_type; }

	public String getSubsets() { return subsets; }

	public void setSubsets(String subsets) { this.subsets = subsets; }
}
