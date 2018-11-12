package com.ixinhoo.papers.entity.base;

import com.chunecai.crumbs.api.entity.AuditEntity;
import jdk.nashorn.internal.runtime.JSONFunctions;
import org.json.JSONObject;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * Created by Administrator on 2018/11/9.
 */
@Table(name="chapter")
public class Chapter extends AuditEntity{

	@Column(name="chapter_id")
	private Integer chapterId;//章节id，唯一标识某个册别下的某个章节
	@Column(name="chapter_name")
	private String chapterName;//章节名称
	@Column(name="child_count")
	private Integer childCount;//子章节数量
	@Column(name="child_data")
	private String childData;//子章节名和子章节id，json串
	@Column(name="path")
	private String path;//完整的章节路径
	@Column(name="parent_id")
	private Integer parentId;//父章节id
	@Column(name="book_id")
	private Integer bookId;//章节所属册别的id
	@Column(name="status")
	private Integer status;//状态，是否有效：0:表示无效，1:表示有效
	@Column(name="order_id")
	private Integer orderId;

	public Integer getChapterId() { return chapterId; }

	public void setChapterId(Integer chapterId) { this.chapterId = chapterId; }

	public String getChapterName() { return chapterName; }

	public void setChapterName(String chapterName) { this.chapterName = chapterName; }

	public Integer getChildCount() { return childCount; }

	public void setChildCount(Integer childCount) {
		this.childCount = childCount;
	}

	public String getChildData() { return childData; }

	public void setChildData(String childData) { this.childData = childData; }

	public String getPath() { return path; }

	public void setPath(String path) { this.path = path; }

	public Integer getParentId() { return parentId;
	}

	public void setParentId(Integer parentId) { this.parentId = parentId; }

	public Integer getBookId() { return bookId; }

	public void setBookId(Integer bookId) { this.bookId = bookId; }

	public Integer getStatus() { return status; }

	public void setStatus(Integer status) { this.status = status; }

	public Integer getOrderId() { return orderId; }

	public void setOrderId(Integer orderId) { this.orderId = orderId; }
}
