package com.ixinhoo.papers.dao.papers;

import com.ixinhoo.papers.dto.papers.PaperTemplateReqDto;

/**
 * 文档动态sql
 *
 * @author 448778074@qq.com (cici)
 */
public class QuestionKnowledgeDaoProvider {


    public String selectByQuestionType(PaperTemplateReqDto reqDto) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT DISTINCT(id) AS id,question_id AS questionId,question_code AS questionCode,knowledge_id AS knowledgeId,knowledge_name AS knowledgeName ");
        sql.append(" FROM question_knowledge ");
        if (reqDto != null) {
            sql.append(" WHERE question_id IN (SELECT id FROM question WHERE audit_status=1 ");
            if (reqDto.getSubjectId() != null && reqDto.getSubjectId() != 0L) {
                sql.append(" AND subject_id=").append(reqDto.getSubjectId());
            }
            if (reqDto.getStage() != null && reqDto.getStage() != 0) {
                sql.append(" AND stage=").append(reqDto.getStage());
            }
            if (reqDto.getVersionId() != null && reqDto.getVersionId() != 0L) {
//                sql.append(" AND version_id=").append(reqDto.getVersionId());
                sql.append(" AND id IN (SELECT paper_id FROM paper_chapter WHERE chapter_id IN(");
                sql.append(" SELECT id FROM chapters WHERE id=").append(reqDto.getVersionId()).append("))");
                if (reqDto.getGrade() != null && reqDto.getGrade() != 0) {
                    sql.append(" AND id IN (SELECT paper_id FROM paper_chapter WHERE chapter_id IN(");
                    sql.append(" SELECT id FROM chapters WHERE (type=3 OR type=4) AND grade=").append(reqDto.getGrade());
                    if (reqDto.getTerm() != null) {
                        sql.append(" AND term=").append(reqDto.getTerm());
                    }
                    sql.append(" ))");
                }
            }
            sql.append(")");
        }
        return sql.toString();
    }


}
