package com.ixinhoo.papers.entity.resources;

import com.chunecai.crumbs.api.entity.AuditEntity;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * 资源文档类
 * Created by Administrator on 2018/11/12.
 */
@Table(name="document_list")
public class DocumentList extends AuditEntity {

	@Column(name="document_id")
	private Integer document_id;//主键
	@Column(name="document_code")
	private Integer document_code;//资源id,与21资源对应
	@Column(name="stage_id")
	private Integer stage_id;//资源所属学段
	@Column(name="subject_id")
	private Integer subject_id;//资源所属学科
	@Column(name="grade_id")
	private Integer grade_id;//资源所属年级
	@Column(name="type")
	private Integer type;//资源类型id，如课件、教案、学案等
	@Column(name="type_name")
	private String type_name;//资源类型名称
	@Column(name="title")
	private String title;//资源标题
	@Column(name="year")
	private String year;//资源所属年份
	@Column(name="user_id")
	private String user_id;//资源上传者id
	@Column(name="user_name")
	private String user_name;//资源上传者名称
	@Column(name="province_id")
	private Integer province_id;//资源适用的省份id
	@Column(name="file_type")
	private String file_type;//文件类型(ppt,doc,rar,video)，none表示未知
	@Column(name="file_size")
	private Integer file_size;//资源文件大小
	@Column(name="format_size")
	private String format_size;//格式化后资源文件大小
	@Column(name="create_at")
	private Integer create_at;//资源上传时间,从21同步过来的
	@Column(name="update_at")
	private Integer update_at;//资源最后更新时间,从21同步过来的
	@Column(name="rank")
	private Integer rank;//资源等级（普通、精品）
	@Column(name="price")
	private Integer price;//资源定价，0表示免费
	@Column(name="book_version_id")
	private Integer book_version_id;//资源所属教材版本
	@Column(name="book_id")
	private Integer book_id;//资源所属册别的id，与book表关联
	@Column(name="chapter_id")
	private Integer chapter_id;//资源所属章节的id，与chapter表关联
	@Column(name="chapter_path")
	private String chapter_path;//资源的章节路径
	@Column(name="status")
	private Integer status;//状态，是否有效：0:表示无效，1:表示有效
	@Column(name="source")
	private Integer source;//资源来源，1-别处引用、2-自己原创
	@Column(name="cover_image")
	private String cover_image;//封面--预留
	@Column(name="keyword")
	private String keyword;//关键字
	@Column(name="copyright")
	private String copyright;//版权信息
	@Column(name="zone_type")
	private Integer zone_type;//资源所属的专区类型，如月考、期中
	@Column(name="is_local")
	private Integer is_local;//是否是本地有的,1:表示本地，0:表示非本地
	@Column(name="stars")
	private Integer stars;//资源的星级--21的字段
	@Column(name="point")
	private Integer point;//资源的价格点数--21的字段

	public Integer getDocument_id() { return document_id; }

	public void setDocument_id(Integer document_id) { this.document_id = document_id; }

	public Integer getDocument_code() { return document_code; }

	public void setDocument_code(Integer document_code) { this.document_code = document_code; }

	public Integer getStage_id() { return stage_id; }

	public void setStage_id(Integer stage_id) { this.stage_id = stage_id; }

	public Integer getSubject_id() { return subject_id; }

	public void setSubject_id(Integer subject_id) { this.subject_id = subject_id; }

	public Integer getGrade_id() { return grade_id; }

	public void setGrade_id(Integer grade_id) { this.grade_id = grade_id; }

	public Integer getType() { return type; }

	public void setType(Integer type) { this.type = type; }

	public String getType_name() { return type_name; }

	public void setType_name(String type_name) { this.type_name = type_name; }

	public String getTitle() { return title; }

	public void setTitle(String title) { this.title = title; }

	public String getYear() { return year; }

	public void setYear(String year) { this.year = year; }

	public String getUser_id() { return user_id; }

	public void setUser_id(String user_id) { this.user_id = user_id; }

	public String getUser_name() { return user_name; }

	public void setUser_name(String user_name) { this.user_name = user_name; }

	public Integer getProvince_id() { return province_id; }

	public void setProvince_id(Integer province_id) { this.province_id = province_id; }

	public String getFile_type() { return file_type; }

	public void setFile_type(String file_type) { this.file_type = file_type; }

	public Integer getFile_size() { return file_size; }

	public void setFile_size(Integer file_size) { this.file_size = file_size; }

	public String getFormat_size() { return format_size; }

	public void setFormat_size(String format_size) { this.format_size = format_size; }

	public Integer getCreate_at() { return create_at; }

	public void setCreate_at(Integer create_at) { this.create_at = create_at; }

	public Integer getUpdate_at() { return update_at; }

	public void setUpdate_at(Integer update_at) { this.update_at = update_at; }

	public Integer getRank() { return rank; }

	public void setRank(Integer rank) { this.rank = rank; }

	public Integer getPrice() { return price; }

	public void setPrice(Integer price) { this.price = price; }

	public Integer getBook_version_id() { return book_version_id; }

	public void setBook_version_id(Integer book_version_id) { this.book_version_id = book_version_id; }

	public Integer getBook_id() { return book_id; }

	public void setBook_id(Integer book_id) { this.book_id = book_id; }

	public Integer getChapter_id() { return chapter_id; }

	public void setChapter_id(Integer chapter_id) { this.chapter_id = chapter_id; }

	public String getChapter_path() { return chapter_path; }

	public void setChapter_path(String chapter_path) { this.chapter_path = chapter_path; }

	public Integer getStatus() { return status; }

	public void setStatus(Integer status) { this.status = status; }

	public Integer getSource() { return source; }

	public void setSource(Integer source) { this.source = source; }

	public String getCover_image() { return cover_image; }

	public void setCover_image(String cover_image) { this.cover_image = cover_image; }

	public String getKeyword() { return keyword; }

	public void setKeyword(String keyword) { this.keyword = keyword; }

	public String getCopyright() { return copyright; }

	public void setCopyright(String copyright) { this.copyright = copyright; }

	public Integer getZone_type() { return zone_type; }

	public void setZone_type(Integer zone_type) { this.zone_type = zone_type; }

	public Integer getIs_local() { return is_local; }

	public void setIs_local(Integer is_local) { this.is_local = is_local; }

	public Integer getStars() { return stars; }

	public void setStars(Integer stars) { this.stars = stars; }

	public Integer getPoint() { return point; }

	public void setPoint(Integer point) { this.point = point; }
}
