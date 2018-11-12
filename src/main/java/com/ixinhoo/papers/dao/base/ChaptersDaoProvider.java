package com.ixinhoo.papers.dao.base;

import org.apache.ibatis.annotations.Param;

/**
 * Created by Administrator on 2018/11/1.
 */
public class ChaptersDaoProvider {

	public String findByChapter(@Param("stage") Integer stage,@Param("subjectId") Long subjectId){
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT * FROM chapters WHERE `stage`=").append(stage).append(" AND `subject_id`=").append(subjectId);
		return sql.toString();
	}
}
