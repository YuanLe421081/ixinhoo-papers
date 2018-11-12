package com.ixinhoo.papers.dao.papers;

import com.google.common.base.Strings;
import com.ixinhoo.papers.dto.papers.PaperTemplateReqDto;
import com.ixinhoo.papers.dto.papers.PapersSearchByChapterReqDto;
import com.ixinhoo.papers.dto.papers.PapersSearchByGradeReqDto;
import org.apache.ibatis.annotations.Param;

/**
 * 文档动态sql
 *
 * @author 448778074@qq.com (cici)
 */
public class PapersDaoProvider {

    /**
     * select * from papers as d
     * private Long typeId;//试卷类型id
     * private String typeName;//试卷类型名称
     * private Integer stage;//学段
     * private Long subjectId;//学科id
     * private Long bookId;//册别id
     * private String bookPath;//册别路径
     * private Long versionId;//版本id
     * private String versionPath;//版本路径
     * private String title;//标题
     * private Integer year;//年份
     * private Integer shareStatus;//是否分享;0用户私有，1通过审核，2,未通过审核,3等待审核，4回收站
     * private Integer auditStatus;//审核状态,3待审核，1审核通过，2审核不通过
     * private String code;//唯一标识
     * private Long createdAt;//创建时间
     * private Long updatedAt;//更新时间
     *
     * @return
     */
    public String selectPapersRecommendByPage(@Param("p") Integer p, @Param("s") Integer s) {
        StringBuilder sql = new StringBuilder();
//        sql.append(" SELECT d.* ");
        sql.append(" SELECT d.id AS id,d.type_id AS typeId,d.type_name AS typeName,d.stage AS stage,d.subject_id AS subjectId ")
                .append(",d.book_id AS bookId,d.book_path AS bookPath,d.version_id AS versionId,d.title AS title,d.year AS year")
                .append(",d.share_status AS shareStatus,d.audit_status AS auditStatus,d.code AS code,d.created_at AS createdAt,d.updated_at AS updatedAt");
        sql.append(" FROM papers as d");
        sql.append(" LEFT JOIN papers_extend as de on d.id=de.id");
        sql.append(" WHERE d.share_status!=0 AND audit_status=1");
        sql.append(" ORDER BY d.updated_at,de.used_num DESC ");
        if (p != null && s != null) {
            sql.append(" LIMIT ").append(p * s).append(",").append(s);
        }
        return sql.toString();
    }

    public String selectDistinctByTemplate(PaperTemplateReqDto reqDto) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT DISTINCT(d.type_id) AS typeId,d.type_name AS typeName");
        sql.append(" FROM papers as d");
        sql.append(" WHERE d.share_status!=0 AND audit_status=1");
        if (reqDto != null) {
            if (reqDto.getSubjectId() != null && reqDto.getSubjectId() != 0L) {
                sql.append(" AND d.subject_id=").append(reqDto.getSubjectId());
            }
            if (reqDto.getStage() != null && reqDto.getStage() != 0) {
                sql.append(" AND d.stage=").append(reqDto.getStage());
            }
            if (reqDto.getVersionId() != null && reqDto.getVersionId() != 0L) {
                sql.append(" AND d.version_id=").append(reqDto.getVersionId());
                if (reqDto.getGrade() != null && reqDto.getGrade() != 0) {
                    sql.append(" AND d.id IN (SELECT paper_id FROM paper_chapter WHERE chapter_id IN(");
                    sql.append(" SELECT id FROM chapters WHERE (type=3 OR type=4) AND grade=").append(reqDto.getGrade());
                    if (reqDto.getTerm() != null) {
                        sql.append(" AND term=").append(reqDto.getTerm());
                    }
                    sql.append(" ))");
                }
            }
        }
        return sql.toString();
    }

    public String selectPaperByTemplate(PaperTemplateReqDto reqDto) {
        StringBuilder sql = new StringBuilder();
        boolean myTemplate = false;
        if (reqDto != null && reqDto.getTypeId() != null && reqDto.getTypeId() != 0L) {
            if (-1 == reqDto.getTypeId()) {//我的模板
                myTemplate = true;
            }
        }
        if (myTemplate) {
            sql.append(" SELECT d.id AS id,-1 AS typeId,'我的模板' AS typeName,d.title AS title,1 AS usedNum");
            sql.append(" FROM user_paper_template as d");
            sql.append(" WHERE d.user_id=").append(reqDto.getUserId()).append(" ");
        } else {
            sql.append(" SELECT d.id AS id,d.type_id AS typeId,d.type_name AS typeName,d.title AS title,de.used_num AS usedNum");
            sql.append(" FROM papers as d");
            sql.append(" LEFT JOIN papers_extend as de on d.id=de.id");
            sql.append(" WHERE d.share_status!=0 AND audit_status=1");
        }
        if (reqDto != null) {
            if (reqDto.getSubjectId() != null && reqDto.getSubjectId() != 0L) {
                sql.append(" AND d.subject_id=").append(reqDto.getSubjectId());
            }
            if (reqDto.getStage() != null && reqDto.getStage() != 0) {
                sql.append(" AND d.stage=").append(reqDto.getStage());
            }
            if (reqDto.getVersionId() != null && reqDto.getVersionId() != 0L) {
                sql.append(" AND d.version_id=").append(reqDto.getVersionId());
                if (reqDto.getGrade() != null && reqDto.getGrade() != 0) {
                    sql.append(" AND d.id IN (SELECT paper_id FROM paper_chapter WHERE chapter_id IN(");
                    sql.append(" SELECT id FROM chapters WHERE (type=3 OR type=4) AND grade=").append(reqDto.getGrade());
                    if (reqDto.getTerm() != null) {
                        sql.append(" AND term=").append(reqDto.getTerm());
                    }
                    sql.append(" ))");
                }
            }
            if (reqDto.getTypeId() != null && reqDto.getTypeId() != 0L) {
                if (-1 == reqDto.getTypeId()) {//我的模板 TODO cici
//                    sql.append(" AND ")
                } else {
                    sql.append(" AND type_id=").append(reqDto.getTypeId());
                }
            }
            if (reqDto.getP() != null && reqDto.getS() != null) {
                sql.append(" LIMIT ").append(reqDto.getP() * reqDto.getS()).append(",").append(reqDto.getS());
            }
        }
        return sql.toString();
    }

    public String selectCountPaperByTemplate(PaperTemplateReqDto reqDto) {
        StringBuilder sql = new StringBuilder();
        boolean myTemplate = false;
        if (reqDto != null && reqDto.getTypeId() != null && reqDto.getTypeId() != 0L) {
            if (-1 == reqDto.getTypeId()) {//我的模板
                myTemplate = true;
            }
        }
        if (myTemplate) {
            sql.append(" SELECT COUNT(d.id) ");
            sql.append(" FROM user_paper_template as d");
            sql.append(" WHERE d.user_id=").append(reqDto.getUserId()).append(" ");
        } else {
            sql.append(" SELECT COUNT(d.id)");
            sql.append(" FROM papers as d");
            sql.append(" WHERE d.share_status!=0 AND audit_status=1");
        }
        if (reqDto != null) {
            if (reqDto.getSubjectId() != null && reqDto.getSubjectId() != 0L) {
                sql.append(" AND d.subject_id=").append(reqDto.getSubjectId());
            }
            if (reqDto.getStage() != null && reqDto.getStage() != 0) {
                sql.append(" AND d.stage=").append(reqDto.getStage());
            }
            if (reqDto.getVersionId() != null && reqDto.getVersionId() != 0L) {
                sql.append(" AND d.version_id=").append(reqDto.getVersionId());
                if (reqDto.getGrade() != null && reqDto.getGrade() != 0) {
                    sql.append(" AND d.id IN (SELECT paper_id FROM paper_chapter WHERE chapter_id IN(");
                    sql.append(" SELECT id FROM chapters WHERE (type=3 OR type=4) AND grade=").append(reqDto.getGrade());
                    if (reqDto.getTerm() != null) {
                        sql.append(" AND term=").append(reqDto.getTerm());
                    }
                    sql.append(" ))");
                }
            }
            if (reqDto.getTypeId() != null && reqDto.getTypeId() != 0L) {
                if (-1 == reqDto.getTypeId()) {//我的模板 TODO cici
//                    sql.append(" AND ")
                } else {
                    sql.append(" AND type_id=").append(reqDto.getTypeId());
                }
            }
        }
        return sql.toString();
    }

    public String selectBySearchChapter(PapersSearchByChapterReqDto reqDto) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT d.id AS id,d.title AS title,de.used_num AS downloadNum,d.updated_at AS updatedAt ");
        sql.append(" FROM papers as d");
        sql.append(" LEFT JOIN papers_extend as de on d.id=de.id");
        sql.append(" WHERE d.share_status!=0 AND audit_status=1");
        if (reqDto != null) {
            if (reqDto.getSubjectId() != null && reqDto.getSubjectId() != 0L) {
                sql.append(" AND d.subject_id=").append(reqDto.getSubjectId());
            }
            if (reqDto.getStage() != null && reqDto.getStage() != 0) {
                sql.append(" AND d.stage=").append(reqDto.getStage());
            }
            if (reqDto.getVersionId() != null && reqDto.getVersionId() != 0L) {
                sql.append(" AND d.version_id=").append(reqDto.getVersionId());
                if (reqDto.getGrade() != null && reqDto.getGrade() != 0) {
                    sql.append(" AND d.id IN (SELECT paper_id FROM paper_chapter WHERE chapter_id IN(");
                    sql.append(" SELECT id FROM chapters WHERE (type=3 OR type=4) AND grade=").append(reqDto.getGrade());
                    if (reqDto.getTerm() != null) {
                        sql.append(" AND term=").append(reqDto.getTerm());
                    }
                    sql.append(" ))");
                }
            }
            if (reqDto.getTypeId() != null && reqDto.getTypeId() != 0L) {
                sql.append(" AND d.type_id=").append(reqDto.getTypeId());
            }
            if (!Strings.isNullOrEmpty(reqDto.getName())) {
                sql.append(" AND d.title LIKE '%").append(reqDto.getName()).append("%'");
            }
            if (reqDto.getProvinceId() != null && reqDto.getProvinceId() != 0L) {
                sql.append(" AND d.id IN (SELECT paper_id FROM paper_province WHERE province_id=0 OR province_id=").append(reqDto.getProvinceId()).append(")");
            }
            if (reqDto.getChapterId() != null && reqDto.getChapterId() != 0L) {
                sql.append(" AND d.id IN (SELECT paper_id FROM paper_chapter WHERE chapter_id=").append(reqDto.getChapterId()).append(")");
            }
            if (!Strings.isNullOrEmpty(reqDto.getSortName())) {
                if ("updatedAt".equalsIgnoreCase(reqDto.getSortName())) {
                    sql.append(" ORDER BY d.updated_at ");
                } else if ("downloadNum".equalsIgnoreCase(reqDto.getSortName())) {
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

    public String selectCountBySearchChapter(PapersSearchByChapterReqDto reqDto) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT COUNT(d.id) ");
        sql.append(" FROM papers as d");
        sql.append(" WHERE d.share_status!=0 AND audit_status=1");
        if (reqDto != null) {
            if (reqDto.getSubjectId() != null && reqDto.getSubjectId() != 0L) {
                sql.append(" AND d.subject_id=").append(reqDto.getSubjectId());
            }
            if (reqDto.getStage() != null && reqDto.getStage() != 0) {
                sql.append(" AND d.stage=").append(reqDto.getStage());
            }
            if (reqDto.getVersionId() != null && reqDto.getVersionId() != 0L) {
                sql.append(" AND d.version_id=").append(reqDto.getVersionId());
                if (reqDto.getGrade() != null && reqDto.getGrade() != 0) {
                    sql.append(" AND d.id IN (SELECT paper_id FROM paper_chapter WHERE chapter_id IN(");
                    sql.append(" SELECT id FROM chapters WHERE (type=3 OR type=4) AND grade=").append(reqDto.getGrade());
                    if (reqDto.getTerm() != null) {
                        sql.append(" AND term=").append(reqDto.getTerm());
                    }
                    sql.append(" ))");
                }
            }
            if (reqDto.getTypeId() != null && reqDto.getTypeId() != 0L) {
                sql.append(" AND d.type_id=").append(reqDto.getTypeId());
            }
            if (!Strings.isNullOrEmpty(reqDto.getName())) {
                sql.append(" AND d.title LIKE '%").append(reqDto.getName()).append("%'");
            }
            if (reqDto.getProvinceId() != null && reqDto.getProvinceId() != 0L) {
                sql.append(" AND d.id IN (SELECT paper_id FROM paper_province WHERE province_id=0 OR province_id=").append(reqDto.getProvinceId()).append(")");
            }
            if (reqDto.getChapterId() != null && reqDto.getChapterId() != 0L) {
                sql.append(" AND d.id IN (SELECT paper_id FROM paper_chapter WHERE chapter_id=").append(reqDto.getChapterId()).append(")");
            }
        }
        return sql.toString();
    }

    public String selectBySearchGrade(PapersSearchByGradeReqDto reqDto) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT d.id AS id,d.title AS title,de.used_num AS downloadNum,d.updated_at AS updatedAt ");
        sql.append(" FROM papers as d");
        sql.append(" LEFT JOIN papers_extend as de on d.id=de.id");
        sql.append(" WHERE d.share_status!=0 AND audit_status=1");
        if (reqDto != null) {
            if (!Strings.isNullOrEmpty(reqDto.getBookName())) {
                sql.append(" AND d.book_id IN (SELECT id FROM chapters WHERE name='").append(reqDto.getName())
                        .append("' AND type=2 )");
            }
            if (!Strings.isNullOrEmpty(reqDto.getName())) {
                sql.append(" AND d.title LIKE '%").append(reqDto.getName()).append("%'");
            }
            if (reqDto.getProvinceId() != null && reqDto.getProvinceId() != 0L) {
                sql.append(" AND d.id IN (SELECT paper_id FROM paper_province WHERE province_id=0 OR province_id=").append(reqDto.getProvinceId()).append(")");
            }
            sql.append(" OR d.book_id=0 ");
            if (!Strings.isNullOrEmpty(reqDto.getSortName())) {
                if ("updatedAt".equalsIgnoreCase(reqDto.getSortName())) {
                    sql.append(" ORDER BY d.updated_at ");
                } else if ("downloadNum".equalsIgnoreCase(reqDto.getSortName())) {
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

    public String selectCountBySearchGrade(PapersSearchByGradeReqDto reqDto) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT COUNT(d.id) ");
        sql.append(" FROM papers as d");
        sql.append(" WHERE d.share_status!=0 AND audit_status=1");
        if (reqDto != null) {
            if (!Strings.isNullOrEmpty(reqDto.getBookName())) {
                sql.append(" AND d.book_id IN (SELECT id FROM chapters WHERE name='").append(reqDto.getName())
                        .append("' AND type=2 )");
            }
            if (!Strings.isNullOrEmpty(reqDto.getName())) {
                sql.append(" AND title LIKE '%").append(reqDto.getName()).append("%'");
            }
            if (reqDto.getProvinceId() != null && reqDto.getProvinceId() != 0L) {
                sql.append(" AND d.id IN (SELECT paper_id FROM paper_province WHERE province_id=0 OR province_id=").append(reqDto.getProvinceId()).append(")");
            }
            sql.append(" OR d.book_id=0 ");
        }
        return sql.toString();
    }

    public String selectGroupByStage() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT stage AS stage,COUNT(id) AS count FROM papers GROUP BY stage");
        return sql.toString();
    }

    public String selectGroupByStageSubject() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT stage AS stage,subject_id AS subjectId,COUNT(id) AS count FROM papers GROUP BY stage,subject_id");
        return sql.toString();
    }

    /**
     *   @Column
    private Long typeId;//试卷类型id
     @Column
     private String typeName;//试卷类型名称
     * @return
     */
    public String selectGroupByPaperType() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT type_id AS id,type_name AS name,COUNT(id) AS count FROM papers GROUP BY type_id");
        return sql.toString();
    }
    public String selectGroupByProvince() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT pp.province_id AS id,pp.province_name AS name,COUNT(d.id) AS count FROM papers d LEFT JOIN paper_province pp ON d.id=pp.paper_id GROUP BY pp.province_id");
        return sql.toString();
    }
    public String selectGroupByStageGrade() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT stage AS stage,grade AS grade, COUNT(id) AS count FROM papers WHERE grade IS NOT NULL GROUP BY grade");
        return sql.toString();
    }
}
