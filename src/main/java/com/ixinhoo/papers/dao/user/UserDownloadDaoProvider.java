package com.ixinhoo.papers.dao.user;

import com.chunecai.crumbs.code.util.string.StringUtil;
import org.apache.ibatis.annotations.Param;

/**
 * 文档动态sql
 *
 * @author 448778074@qq.com (cici)
 */
public class UserDownloadDaoProvider {

    /**
     * 每天、每周、每月--资料下载会员
     *
     * @param beginTime 开始时间
     * @param endTime   结束时间
     * @param dayType   类型，1-天、2-周、3-月
     * @return
     */
    public String selectGroupByTypeAndTime(@Param("beginTime") Long beginTime,
                                           @Param("endTime") Long endTime,
                                           @Param("dayType") Integer dayType) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        if (dayType == 1) {
            sql.append(" FROM_UNIXTIME");
            sql.append(" (time/1000, \"%Y-%m-%d\"").append(" ) AS day , count(id) AS count FROM user_download ");
        } else if (dayType == 2) {
            sql.append(" WEEK(FROM_UNIXTIME");
            sql.append(" (time/1000, \"%Y-%m-%d\"").append(" )) AS day , count(id) AS count FROM user_download ");
        } else {
            sql.append(" MONTH(FROM_UNIXTIME");
            sql.append(" (time/1000, \"%Y-%m-%d\"").append(" )) AS day , count(id) AS count FROM user_download ");
        }
        sql.append(" WHERE time<=").append(endTime)
                .append(" AND time>=").append(beginTime).append(" ");
        sql.append(" GROUP BY day ");
        return sql.toString();
    }
    /**
     * 每小时
     *
     * @param beginTime 开始时间
     * @param endTime   结束时间
     * @return
     */
    public String selectGroupByHourAndTime(@Param("beginTime") Long beginTime,
                                           @Param("endTime") Long endTime) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT  COUNT(id) AS count,FROM_UNIXTIME(time/1000,'%H') AS day from user_download ");
        if(beginTime!=null||endTime!=null){
            sql.append(" WHERE ");
            if(endTime!=null){
                sql.append(" time<=").append(endTime);
            }
            if(beginTime!=null){
                if(endTime!=null){
                    sql.append(" AND");
                }
                sql.append(" time>=").append(beginTime);
            }
        }
        sql.append(" GROUP BY day ");
        return sql.toString();
    }

    public String selectGroupByType() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT type AS id,COUNT(id) AS count FROM user_download GROUP BY type");
        return sql.toString();
    }

    public String selectGroupByStage() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT do.stage AS id,COUNT(d.id) AS count FROM user_download as d LEFT JOIN document AS do ON d.data_id=do.id GROUP BY do.stage");
        return sql.toString();
    }

    public String selectGroupByTeach() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT do.teaching_age AS id,COUNT(d.id) AS count FROM user_download as d LEFT JOIN user AS do ON d.user_id=do.id GROUP BY do.teaching_age");
        return sql.toString();
    }
    public String selectGroupByStageSubject() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT do.stage AS stage,do.subject_id AS subjectId,do.subject_name AS subjectName,COUNT(d.id) AS count FROM user_download as d LEFT JOIN user AS do ON d.user_id=do.id GROUP BY do.stage,do.subject_id");
        return sql.toString();
    }
    public String selectGroupByProvince() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT do.province_id AS id,do.province_name AS name,COUNT(d.id) AS count FROM user_download as d LEFT JOIN document AS do ON d.data_id=do.id GROUP BY do.province_id");
        return sql.toString();
    }

    /**
     * 资源年级
     * @return
     */
    public String selectGroupByStageGrade() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT do.stage AS stage,do.grade AS grade, COUNT(d.id) AS count FROM user_download as d LEFT JOIN document AS do ON d.data_id=do.id WHERE do.grade IS NOT NULL GROUP BY do.grade");
        return sql.toString();
    }


}
