package com.ixinhoo.papers.dao.resources;

import com.chunecai.crumbs.code.util.collection.Collections3;
import com.google.common.base.Strings;
import com.ixinhoo.papers.dto.resources.DocumentSearchReqDto;
import com.ixinhoo.papers.entity.EntitySetting;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 文档动态sql
 *
 * @author 448778074@qq.com (cici)
 */
public class DocumentDaoProvider {

    /**
     * select * from document as d
     * left join document_extend as de on d.id=de.id
     * where term!=1 and subject_id=1 and province_id=2 and
     * (
     * version_id in(select distinct(version_id) from chapters where type=1 and grade=2)
     * OR version_id in(select DISTINCT(version_id) from user_download where user_id=1)
     * )
     * ORDER BY d.coin,de.download_num limit 0,5
     *
     * @param grade
     * @param subjectId
     * @param provinceId
     * @param userId
     * @param upHalf
     * @return
     */
    public String selectByHomeRecommend(@Param("grade") Integer grade, @Param("subjectId") Long subjectId, @Param("provinceId")  Long provinceId, @Param("userId") Long userId,@Param("upHalf")  Boolean upHalf) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT d.id as id,d.subject_id as subjectId,d.subject_name as subjectName,d.version_id as versionId,d.version_name as versionName,d.stage as stage,d.type_id as typeId,d.type_name as typeName,d.term as term,d.cover_image as coverImage,d.title as title");
        sql.append(" FROM document as d");
        sql.append(" LEFT JOIN document_extend as de on d.id=de.id");
        sql.append(" WHERE status=1 ");
        if (upHalf == null) {
//            sql.append(" AND 1=1 ");
        } else if (upHalf) {//true为上半年，下学期
            sql.append(" AND term!=1 ");
        } else {
            sql.append(" AND term!=2 ");
        }
        if (subjectId != null && subjectId != 0L) {
            sql.append(" AND subject_id=").append(subjectId);
        }
        if (provinceId != null && provinceId != 0L) {
            sql.append(" AND province_id=").append(provinceId);
        }
        if (grade != null && grade != 0L) {
            sql.append(" AND (version_id in (SELECT DISTINCT(version_id)  FROM chapters WHERE type=1 AND grade=").append(grade).append(")");
            if (userId != null && userId != 0L) {
                sql.append(" OR version_id in (SELECT DISTINCT(version_id) FROM user_download WHERE user_id=").append(userId).append(")");
            }
            sql.append(")");
        }
        sql.append(" ORDER BY d.coin,de.download_num DESC LIMIT 0,5");
        return sql.toString();
    }

    /**
     * {"p":0,"s":10,"stage":0,"subjectId":0,"versionId":0,"typeId":0,"name":"","sort":"updatedAt","desc":"desc"},
     *
     * @param reqDto
     * @return
     */
    public String selectBySearch(DocumentSearchReqDto reqDto) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT d.id as id,d.subject_id as subjectId,d.subject_name as subjectName,d.version_id as versionId,d.version_name as versionName,d.stage as stage,d.type_id as typeId,d.type_name as typeName,d.term as term,d.cover_image as coverImage,d.title as title,");
        sql.append("d.user_id as userId,d.user_name as userName,d.coin as coin,d.updated_at as updatedAt,d.created_at as createdAt,de.download_num as downloadNum,d.intro as intro,de.score as score,de.score_num as scoreNum,d.file_type as fileType,d.year as year");
        sql.append(" FROM document as d");
        sql.append(" LEFT JOIN document_extend as de on d.id=de.id");
        sql.append(" WHERE status=1 ");
        if (reqDto != null) {
            if (reqDto.getStage() != null && reqDto.getStage() != 0) {
                sql.append(" AND stage=").append(reqDto.getStage());
            }
            if (reqDto.getSubjectId() != null && reqDto.getSubjectId() != 0L) {
                sql.append(" AND subject_id=").append(reqDto.getSubjectId());
            }
            if (Collections3.isNotEmpty(reqDto.getTypeIds())) {
                sql.append(" AND type_id IN (").append(  String.join(",", reqDto.getTypeIds().stream().map(d -> d.toString()).collect(Collectors.toList()))).append(")");
            }
            if (reqDto.getChapterId() != null && reqDto.getChapterId() != 0L) {
                sql.append(" AND d.id IN (SELECT document_id FROM document_chapter WHERE chapter_id=").append(reqDto.getChapterId()).append(")");
            }else{
                if (reqDto.getVersionId() != null && reqDto.getVersionId() != 0L) {
                    sql.append(" AND version_id=").append(reqDto.getVersionId());
                }
            }
            if(reqDto.getProvinceId()!=null&&reqDto.getProvinceId()!=0L){
                sql.append(" AND (d.province_id=").append(reqDto.getProvinceId()).append(" OR d.province_id=0)");
            }
            if(!Strings.isNullOrEmpty(reqDto.getName())){
                sql.append(" AND d.title LIKE '%").append(reqDto.getName()).append("%'");
            }
            if(!Strings.isNullOrEmpty(reqDto.getSpecialArea())){//备考查询
                sql.append(" AND d.id IN (SELECT document_id FROM document_chapter WHERE chapter_id IN(SELECT id FROM chapters WHERE type=")
                        .append(EntitySetting.CHAPTERS_TYPE_EXAM);
                if(reqDto.getGrade()!=null&&reqDto.getGrade()!=0L){
                    sql.append(" AND grade=").append(reqDto.getGrade());
                }
                sql.append(" AND name='").append(reqDto.getSpecialArea()).append("'))");
            }
            if (!Strings.isNullOrEmpty(reqDto.getSortName())) {
                if ("updatedAt".equalsIgnoreCase(reqDto.getSortName())) {
                    sql.append(" ORDER BY d.updated_at ");
                } else if ("downloadNum".equalsIgnoreCase(reqDto.getSortName())) {
                    sql.append(" ORDER BY de.download_num ");
                } else if ("score".equalsIgnoreCase(reqDto.getSortName())) {
                    sql.append(" ORDER BY de.score ");
                } else if ("price".equalsIgnoreCase(reqDto.getSortName())) {
                    sql.append(" ORDER BY d.coin ");
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

    /**
     * {"p":0,"s":10,"stage":0,"subjectId":0,"versionId":0,"typeId":0,"name":"","sort":"updatedAt","desc":"desc"},
     *
     * @param reqDto
     * @return
     */
    public String selectCountBySearch(DocumentSearchReqDto reqDto) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT COUNT(d.id)");
        sql.append(" FROM document as d");
//        sql.append(" LEFT JOIN document_extend as de on d.id=de.id");
        sql.append(" WHERE status=1 ");
        if (reqDto != null) {
            if (reqDto.getStage() != null && reqDto.getStage() != 0) {
                sql.append(" AND stage=").append(reqDto.getStage());
            }
            if (reqDto.getSubjectId() != null && reqDto.getSubjectId() != 0L) {
                sql.append(" AND subject_id=").append(reqDto.getSubjectId());
            }
            if (Collections3.isNotEmpty(reqDto.getTypeIds())) {
                sql.append(" AND type_id IN (").append(  String.join(",", reqDto.getTypeIds().stream().map(d -> d.toString()).collect(Collectors.toList()))).append(")");
            }
            if (reqDto.getChapterId() != null && reqDto.getChapterId() != 0L) {
                sql.append(" AND d.id IN (SELECT document_id FROM document_chapter WHERE chapter_id=").append(reqDto.getChapterId()).append(")");
            }else{
                if (reqDto.getVersionId() != null && reqDto.getVersionId() != 0L) {
                    sql.append(" AND version_id=").append(reqDto.getVersionId());
                }
            }
            if(reqDto.getProvinceId()!=null&&reqDto.getProvinceId()!=0L){
                sql.append(" AND (d.province_id=").append(reqDto.getProvinceId()).append(" OR d.province_id=0)");
            }
            if(!Strings.isNullOrEmpty(reqDto.getName())){
                sql.append(" AND d.title LIKE '%").append(reqDto.getName()).append("%'");
            }
            if(!Strings.isNullOrEmpty(reqDto.getSpecialArea())){//备考查询
                sql.append(" AND d.id IN (SELECT document_id FROM document_chapter WHERE chapter_id IN(SELECT id FROM chapters WHERE type=")
                        .append(EntitySetting.CHAPTERS_TYPE_EXAM);
                if(reqDto.getGrade()!=null&&reqDto.getGrade()!=0L){
                    sql.append(" AND grade=").append(reqDto.getGrade());
                }
                sql.append(" AND name='").append(reqDto.getSpecialArea()).append("'))");
            }
        }
        return sql.toString();
    }

    /**
     * 根据id查询同类型版本推荐
     *
     * @return
     */
    public String selectSameTypeById(@Param("chapterIds") List<Long> chapterIds,
                                     @Param("versionId") Long versionId,
                                     @Param("typeId") Integer typeId,
                                     @Param("hasCoin")Boolean hasCoin) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT d.id as id,d.subject_id as subjectId,d.subject_name as subjectName,d.version_id as versionId,d.version_name as versionName,d.stage as stage,d.type_id as typeId,d.type_name as typeName,d.term as term,d.cover_image as coverImage,d.title as title,");
        sql.append("d.user_id as userId,d.user_name as userName,d.coin as coin,d.updated_at as updatedAt,d.created_at as createdAt,de.download_num as downloadNum,d.intro as intro,de.score as score,de.score_num as scoreNum,d.file_type as fileType,d.year as year");
        sql.append(" FROM document as d");
        sql.append(" LEFT JOIN document_extend as de on d.id=de.id");
        sql.append(" WHERE status=1 ");
        if (versionId != null && versionId != 0L) {
            sql.append(" AND d.version_id=").append(versionId);
        }
        if (Collections3.isNotEmpty(chapterIds)) {
            sql.append(" AND d.id IN(SELECT document_id FROM document_chapter where chapter_id IN(").append(
                    String.join(",", chapterIds.stream().map(d -> d.toString()).collect(Collectors.toList()))
            ).append("))");
        }
        if (typeId != null && typeId != 0L) {
            sql.append(" AND d.type_id=").append(typeId);
        }
        if (hasCoin != null && hasCoin) {
            sql.append(" AND d.coin IS NOT NULL AND d.coin!=0 ");
        }
        sql.append(" ORDER BY de.download_num DESC");
        sql.append(" LIMIT 0,5");
        return sql.toString();
    }

    /**
     * 底部猜你喜欢
     *
     * @return
     */
    public String selectGuessByIdAndPage(@Param("chapterIds")List<Long> chapterIds,
                                         @Param("versionId") Long versionId,
                                         @Param("typeId") Integer typeId,
                                         @Param("p")Integer p,
                                         @Param("s")Integer s){
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT d.id as id,d.subject_id as subjectId,d.subject_name as subjectName,d.version_id as versionId,d.version_name as versionName,d.stage as stage,d.type_id as typeId,d.type_name as typeName,d.term as term,d.cover_image as coverImage,d.title as title,");
        sql.append("d.user_id as userId,d.user_name as userName,d.coin as coin,d.updated_at as updatedAt,d.created_at as createdAt,de.download_num as downloadNum,d.intro as intro,de.score as score,de.score_num as scoreNum,d.file_type as fileType,d.year as year");
        sql.append(" FROM document as d");
        sql.append(" LEFT JOIN document_extend as de on d.id=de.id");
        sql.append(" WHERE status=1 ");
        if (versionId != null && versionId != 0L) {
            sql.append(" AND d.version_id!=").append(versionId);
        }
        if (Collections3.isNotEmpty(chapterIds)) {
            sql.append(" AND d.id IN(SELECT document_id FROM document_chapter where chapter_id IN(").append(
                    String.join(",", chapterIds.stream().map(d -> d.toString()).collect(Collectors.toList()))
            ).append("))");
        }
        if (typeId != null && typeId != 0L) {
            sql.append(" AND d.type_id=").append(typeId);
        }
        sql.append(" ORDER BY de.download_num DESC");
        if (p != null && s != null) {
            sql.append(" LIMIT ").append(p * s).append(",").append(s);
        }
        return sql.toString();
    }

    /**
     * 下载成功--资料推荐
     *
     * @return
     */
    public String selectDownloadRecommendByIdAndPage(@Param("chapterIds") List<Long> chapterIds,
                                                     @Param("versionIds")List<Long> versionIds,
                                                     @Param("typeId")Integer typeId,
                                                     @Param("p")Integer p,
                                                     @Param("s")Integer s) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT d.id as id,d.subject_id as subjectId,d.subject_name as subjectName,d.version_id as versionId,d.version_name as versionName,d.stage as stage,d.type_id as typeId,d.type_name as typeName,d.term as term,d.cover_image as coverImage,d.title as title,");
        sql.append("d.user_id as userId,d.user_name as userName,d.coin as coin,d.updated_at as updatedAt,d.created_at as createdAt,de.download_num as downloadNum,d.intro as intro,de.score as score,de.score_num as scoreNum,d.file_type as fileType,d.year as year");
        sql.append(" FROM document as d");
        sql.append(" LEFT JOIN document_extend as de on d.id=de.id");
        sql.append(" WHERE status=1 ");
        if (Collections3.isNotEmpty(versionIds)) {
            sql.append(" AND d.version_id NOT IN (").append(String.join(",", versionIds.stream().map(d -> d.toString()).collect(Collectors.toList()))).append(")");
        }
        if (Collections3.isNotEmpty(chapterIds)) {
            sql.append(" AND d.id IN(SELECT document_id FROM document_chapter where chapter_id IN(").append(
                    String.join(",", chapterIds.stream().map(d -> d.toString()).collect(Collectors.toList()))
            ).append("))");
        }
        if (typeId != null && typeId != 0L) {
            sql.append(" AND d.type_id=").append(typeId);
        }
        sql.append(" ORDER BY de.download_num DESC");
        if (p != null && s != null) {
            sql.append(" LIMIT ").append(p * s).append(",").append(s);
        }
        return sql.toString();
    }
    public String selectGroupByType() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT type_id AS id,type_name AS name,COUNT(id) AS count FROM document GROUP BY type_id ");
        return sql.toString();
    }
    public String selectGroupByStage() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT stage AS id,COUNT(id) AS count FROM document GROUP BY stage");
        return sql.toString();
    }
    public String selectGroupByStageSubject() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT stage AS stage,subject_id AS subjectId,subject_name AS subjectName,COUNT(id) AS count FROM document GROUP BY stage,subject_id");
        return sql.toString();
    }
    public String selectGroupByStageGrade() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT stage AS stage,grade AS grade, COUNT(id) AS count FROM document WHERE grade IS NOT NULL GROUP BY grade");
        return sql.toString();
    }
    public String selectGroupByProvince() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT province_id AS id,province_name AS name,COUNT(id) AS count FROM document GROUP BY province_id");
        return sql.toString();
    }
}