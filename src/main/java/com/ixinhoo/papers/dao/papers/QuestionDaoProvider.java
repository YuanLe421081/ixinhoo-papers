package com.ixinhoo.papers.dao.papers;

import com.chunecai.crumbs.code.util.collection.Collections3;
import com.google.common.base.Strings;
import com.ixinhoo.papers.dto.papers.QuestionSearchReqDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 文档动态sql
 *
 * @author 448778074@qq.com (cici)
 */
public class QuestionDaoProvider {


    /**
     * {"p":0,"s":10,"stage":0,"subjectId":0,"grade":0,"term":0,"versionId":0,
     * "chapterId":0,"examType":0,"typeId":0,"difficult":0,"knowledgeNum":0,
     * "name":"","sortName":"updatedAt","desc":"desc"},
     * private Long id;//主键
     * private Long paperId;//试卷主键id
     * private Integer baseType;//基础题型
     * private String baseTypeName;//基础题型名称
     * private Long typeId;//题目题型id，为QuestionType中的id
     * private String typeName;//题目题型名称
     * private Integer stage;//学段
     * private Long subjectId;//学科id
     * private Integer difficult;//题目难度值
     * private String difficultName;//难度名称
     * private Integer examType;//考察类型
     * private String examTypeName;//考察类型名称
     * private Integer grade;//适用年级
     * private String source;//试卷  来源
     * private String content;//题目内容
     * private Integer knowledgeNum;//知识点个数
     * private Long usedNum;//组卷次数
     * private Boolean isCollection=false;//是否收藏
     *
     * @param reqDto
     * @return
     */
    public String selectBySearch(QuestionSearchReqDto reqDto) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT d.id AS id,d.options AS options,d.paper_id AS paperId,d.base_type AS baseType,d.base_type_name AS baseTypeName,d.type_id AS typeId,d.type_name AS typeName ")
                .append(",d.stage AS stage,d.subject_id AS subjectId,d.difficult AS difficult,d.difficult_name AS difficultName")
                .append(",d.exam_type AS examType,d.exam_type_name AS examTypeName,d.grade AS grade,d.source AS source,d.content AS content")
                .append(",d.knowledge_num AS knowledgeNum,de.used_num");
        sql.append(" FROM question AS d");
        sql.append(" LEFT JOIN question_extend as de on d.id=de.id");
        sql.append(" WHERE audit_status=1 ");
        if (reqDto != null) {
            if (reqDto.getStage() != null && reqDto.getStage() != 0) {
                sql.append(" AND stage=").append(reqDto.getStage());
            }
            if (reqDto.getSubjectId() != null && reqDto.getSubjectId() != 0L) {
                sql.append(" AND subject_id=").append(reqDto.getSubjectId());
            }
            if (reqDto.getKnowledgeId() != null && reqDto.getKnowledgeId() != 0L) {
                sql.append(" AND d.id IN (SELECT question_id FROM question_knowledge WHERE knowledge_id =")
                        .append(reqDto.getKnowledgeId()).append(") ");
            }
            if (reqDto.getTypeId() != null && reqDto.getTypeId() != 0L) {
                sql.append(" AND type_id=").append(reqDto.getTypeId());
            }
            if (reqDto.getExamType() != null && reqDto.getExamType() != 0) {
                sql.append(" AND exam_type=").append(reqDto.getExamType());
            }
            if (reqDto.getDifficult() != null && reqDto.getDifficult() != 0) {
                sql.append(" AND difficult=").append(reqDto.getDifficult());
            }
            if (reqDto.getKnowledgeNum() != null && reqDto.getKnowledgeNum() != 0) {
                if (reqDto.getKnowledgeNum() == 1) {
                    sql.append(" AND knowledge_num=1");
                } else if (reqDto.getKnowledgeNum() == 2) {
                    sql.append(" AND knowledge_num=2");
                } else {
                    sql.append(" AND knowledge_num>2");
                }
            }
            if (!Strings.isNullOrEmpty(reqDto.getName())) {
                sql.append(" AND d.content LIKE '%").append(reqDto.getName()).append("%'");
            }
            if (reqDto.getChapterId() != null && reqDto.getChapterId() != 0L) {
                sql.append(" AND d.id IN (SELECT question_id FROM question_chapter WHERE chapter_id=").append(reqDto.getChapterId()).append(")");
            }else if (reqDto.getVersionId() != null && reqDto.getVersionId() != 0L) {
                sql.append(" AND d.id IN (SELECT question_id FROM question_chapter WHERE chapter_id =")
                        .append(reqDto.getVersionId()).append(") ");
            }
            if (!Strings.isNullOrEmpty(reqDto.getSortName())) {
                if ("updatedAt".equalsIgnoreCase(reqDto.getSortName())) {
                    sql.append(" ORDER BY d.created_at ");
                } else if ("usedNum".equalsIgnoreCase(reqDto.getSortName())) {
                    sql.append(" ORDER BY de.used_num ");
                }
                if (!Strings.isNullOrEmpty(reqDto.getDesc())) {
                    if ("desc".equalsIgnoreCase(reqDto.getDesc())) {
                        sql.append(" DESC");
                    } else {
                        sql.append(" ASC");
                    }
                }

            }
            if (reqDto.getP() != null && reqDto.getS() != null) {
                sql.append(" LIMIT ").append(reqDto.getP() * reqDto.getS()).append(",").append(reqDto.getS());
            }
        }
        return sql.toString();
    }


    public String selectCountBySearch(QuestionSearchReqDto reqDto) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT COUNT(d.id) ");
        sql.append(" FROM question AS d");
//        sql.append(" LEFT JOIN question_extend as de on d.id=de.id");
        sql.append(" WHERE audit_status=1 ");
        if (reqDto != null) {
            if (reqDto.getStage() != null && reqDto.getStage() != 0) {
                sql.append(" AND stage=").append(reqDto.getStage());
            }
            if (reqDto.getSubjectId() != null && reqDto.getSubjectId() != 0L) {
                sql.append(" AND subject_id=").append(reqDto.getSubjectId());
            }
            if (reqDto.getKnowledgeId() != null && reqDto.getKnowledgeId() != 0L) {
                sql.append(" AND id IN (SELECT question_id FROM question_knowledge WHERE knowledge_id =")
                        .append(reqDto.getKnowledgeId()).append(") ");
            }
            if (reqDto.getTypeId() != null && reqDto.getTypeId() != 0L) {
                sql.append(" AND type_id=").append(reqDto.getTypeId());
            }
            if (reqDto.getExamType() != null && reqDto.getExamType() != 0) {
                sql.append(" AND exam_type=").append(reqDto.getExamType());
            }
            if (reqDto.getDifficult() != null && reqDto.getDifficult() != 0) {
                sql.append(" AND difficult=").append(reqDto.getDifficult());
            }
            if (reqDto.getKnowledgeNum() != null && reqDto.getKnowledgeNum() != 0) {
                if (reqDto.getKnowledgeNum() == 1) {
                    sql.append(" AND knowledge_num=1");
                } else if (reqDto.getKnowledgeNum() == 2) {
                    sql.append(" AND knowledge_num=2");
                } else {
                    sql.append(" AND knowledge_num>2");
                }
            }
            if (!Strings.isNullOrEmpty(reqDto.getName())) {
                sql.append(" AND d.content LIKE '%").append(reqDto.getName()).append("%'");
            }
            if (reqDto.getChapterId() != null && reqDto.getChapterId() != 0L) {
                sql.append(" AND d.id IN (SELECT question_id FROM question_chapter WHERE chapter_id=").append(reqDto.getChapterId()).append(")");
            }else  if (reqDto.getVersionId() != null && reqDto.getVersionId() != 0L) {
                sql.append(" AND d.id IN (SELECT question_id FROM question_chapter WHERE chapter_id =")
                        .append(reqDto.getVersionId()).append(") ");
            }
        }
        return sql.toString();
    }

    /**
     * select count(d.id),d.type_id,d.type_name from question as d group by d.type_id;
     *
     * @param ids
     * @return
     */
    public String selectCountByChapterIdsGroupByType(@Param("ids") List<Long> ids, @Param("other") Boolean other) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT COUNT(d.id) AS num,d.type_id AS id,d.type_name AS name ");
        sql.append(" FROM question AS d");
        sql.append(" WHERE audit_status=1 ");
        if (Collections3.isNotEmpty(ids)) {
            sql.append(" AND d.id ");
            if (other != null && other) {
                sql.append(" NOT ");
            }
            sql.append(" IN(SELECT question_id FROM question_chapter WHERE chapter_id IN(")
                    .append(String.join(",", ids.stream().map(d -> d.toString()).collect(Collectors.toList())))
                    .append("))");
        }
        return sql.toString();
    }

    public String selectByTypeIdAndChapterAndNum(@Param("typeId") Long typeId, @Param("chapterIds") List<Long> chapterIds, @Param("difficult") Integer difficult, @Param("num") Integer num) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT d.* ");
        sql.append(" FROM question AS d");
        sql.append(" WHERE audit_status=1 ");
        if (typeId != null && typeId != 0L) {
            sql.append(" AND type_id=").append(typeId);
            if (difficult != null) {
                sql.append(" AND difficult=").append(difficult);
            }
            if (Collections3.isNotEmpty(chapterIds)) {
                sql.append(" AND d.id  IN(SELECT question_id FROM question_chapter WHERE chapter_id IN(")
                        .append(String.join(",", chapterIds.stream().map(d -> d.toString()).collect(Collectors.toList())))
                        .append("))");
            }
        }
        if (num != null) {
            sql.append(" LIMIT 0,").append(num);
        }
        return sql.toString();
    }

    public String selectByTypeIdAndKnowledgeAndNum(@Param("typeId") Long typeId, @Param("knowledgeIds") List<Long> knowledgeIds, @Param("difficult") Integer difficult, @Param("num") Integer num) {
        StringBuilder sql = new StringBuilder();
//        sql.append(" SELECT d.* ");
        sql.append(" SELECT d.id AS id,d.options AS options,d.paper_id AS paperId,d.base_type AS baseType,d.base_type_name AS baseTypeName,d.type_id AS typeId,d.type_name AS typeName ")
                .append(",d.stage AS stage,d.subject_id AS subjectId,d.difficult AS difficult,d.difficult_name AS difficultName")
                .append(",d.exam_type AS examType,d.exam_type_name AS examTypeName,d.grade AS grade,d.source AS source,d.content AS content")
                .append(",d.knowledge_num AS knowledgeNum");
        sql.append(" FROM question AS d");
        sql.append(" WHERE audit_status=1 ");
        if (typeId != null && typeId != 0L) {
            sql.append(" AND type_id=").append(typeId);
            if (difficult != null) {
                sql.append(" AND difficult=").append(difficult);
            }
            if (Collections3.isNotEmpty(knowledgeIds)) {
                sql.append(" AND d.id  IN(SELECT question_id FROM question_knowledge WHERE knowledge_id IN(")
                        .append(String.join(",", knowledgeIds.stream().map(d -> d.toString()).collect(Collectors.toList())))
                        .append("))");
            }
        }
        if (num != null) {
            sql.append(" LIMIT 0,").append(num);
        }
        return sql.toString();
    }

    public String selectGroupByDifficult() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT difficult AS id, difficult_name AS name,COUNT(id) AS count FROM question GROUP BY difficult");
        return sql.toString();
    }

    public String selectGroupByStage() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT stage AS id, COUNT(id) AS count FROM question GROUP BY stage");
        return sql.toString();
    }

    /**
     * private Integer stage;//学段
     * private Integer grade;//年级
     * private Integer count;//总数
     *
     * @return
     */
    public String selectGroupByStageGrade() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT stage AS stage,grade AS grade, COUNT(id) AS count FROM question GROUP BY grade");
        return sql.toString();
    }

    public String selectGroupByStageSubject() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT stage AS stage,subject_id AS subjectId,COUNT(id) AS count FROM question GROUP BY stage,subject_id");
        return sql.toString();
    }

}
