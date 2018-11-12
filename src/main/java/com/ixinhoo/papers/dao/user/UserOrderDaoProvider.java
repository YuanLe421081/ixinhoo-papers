package com.ixinhoo.papers.dao.user;

import com.chunecai.crumbs.code.util.string.StringUtil;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 文档动态sql
 *
 * @author 448778074@qq.com (cici)
 */
public class UserOrderDaoProvider {


    public String selectCountByAddUser(@Param("beginTime") Long beginTime,
                                       @Param("endTime") Long endTime) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT COUNT(d.id) ");
        sql.append(" FROM user as d");
        sql.append(" WHERE id IN(SELECT user_id FROM user_order WHERE time<=").append(endTime)
                .append(" AND time>=").append(beginTime).append(") ");
        return sql.toString();
    }

    public String selectSumMoneyInType(@Param("beginTime") Long beginTime,
                                       @Param("endTime") Long endTime,
                                       @Param("type") List<Integer> type) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT SUM(d.price) ");
        sql.append(" FROM user_order as d");
        sql.append(" WHERE time<=").append(endTime).append(" AND time>=").append(beginTime)
                .append(" AND status=1 AND d.price IS NOT NULL AND ")
                .append(" type IN (").append(StringUtil.listJoinChart(type, ",")).append(")");
        return sql.toString();
    }
    public String selectSumCoinInType(@Param("beginTime") Long beginTime,
                                       @Param("endTime") Long endTime,
                                       @Param("type") List<Integer> type) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT SUM(d.coin) ");
        sql.append(" FROM user_order as d");
        sql.append(" WHERE ")
                .append(" status=1 AND d.coin IS NOT NULL AND ")
                .append(" type IN (").append(StringUtil.listJoinChart(type, ",")).append(")");
        if(endTime!=null){
            sql.append(" AND time<=").append(endTime);
        }
        if(beginTime!=null){
            sql.append(" AND time>=").append(beginTime);
        }
        return sql.toString();
    }

    public String selectSumMoneyGroupByHourAndTime(@Param("beginTime") Long beginTime,
                                       @Param("endTime") Long endTime) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT  SUM(price) AS count,FROM_UNIXTIME(time/1000,'%H') AS day from user_order ");
        if(beginTime!=null||endTime!=null){
            sql.append(" WHERE status=1 AND price IS NOT NULL AND ");
            sql.append(" type=1 ");
            if(endTime!=null){
                sql.append(" AND time<=").append(endTime);
            }
            if(beginTime!=null){
                sql.append(" AND time>=").append(beginTime);
            }
        }
        sql.append(" GROUP BY day ");
        return sql.toString();
    }

    public String selectSumLessMoneyGroupByHourAndTime(@Param("beginTime") Long beginTime,
                                       @Param("endTime") Long endTime) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT  SUM(price) AS count,FROM_UNIXTIME(time/1000,'%H') AS day from user_order ");
        if(beginTime!=null||endTime!=null){
            sql.append(" WHERE status=1 AND price IS NOT NULL AND ");
            sql.append(" type!=1 ");
            if(endTime!=null){
                sql.append(" AND time<=").append(endTime);
            }
            if(beginTime!=null){
                sql.append(" AND time>=").append(beginTime);
            }
        }
        sql.append(" GROUP BY day ");
        return sql.toString();
    }
    public String selectSumCoinGroupByHourAndTime(@Param("beginTime") Long beginTime,
                                       @Param("endTime") Long endTime,
                                       @Param("type") List<Integer> type) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT  SUM(coin) AS count,FROM_UNIXTIME(time/1000,'%H') AS day from user_order ");
        if(beginTime!=null||endTime!=null){
            sql.append(" WHERE status=1 AND coin IS NOT NULL AND ");
            sql.append(" type IN (").append(StringUtil.listJoinChart(type, ",")).append(")");
            if(endTime!=null){
                sql.append(" AND time<=").append(endTime);
            }
            if(beginTime!=null){
                sql.append(" AND time>=").append(beginTime);
            }
        }
        sql.append(" GROUP BY day ");
        return sql.toString();
    }

    public String selectSumGroupByDocumentType() {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT SUM(d.coin) AS count,do.type_id AS id");
        sql.append(" FROM user_order AS d LEFT JOIN document AS do ON d.data_id=do.id ");
        sql.append(" WHERE d.type=2 AND d.status=1 GROUP BY do.type_id");
        return sql.toString();
    }

    public String selectSumGroupByDocumentStage() {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT SUM(d.coin) AS count,do.stage AS id");
        sql.append(" FROM user_order AS d LEFT JOIN document AS do ON d.data_id=do.id ");
        sql.append(" WHERE d.type=2 AND d.status=1 GROUP BY do.stage");
        return sql.toString();
    }

    public String selectSumGroupByDocumentProvince() {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT SUM(d.coin) AS count,do.province_id AS id");
        sql.append(" FROM user_order AS d LEFT JOIN document AS do ON d.data_id=do.id ");
        sql.append(" WHERE d.type=2 AND d.status=1 GROUP BY do.province_id ");
        return sql.toString();
    }

    /**
     * 用户的教龄为下载教龄
     *
     * @return
     */
    public String selectSumGroupByUserTeach() {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT SUM(d.coin) AS count,do.teaching_age AS id");
        sql.append(" FROM user_order AS d LEFT JOIN user AS do ON d.user_id=do.id ");
        sql.append(" WHERE d.type=2 AND d.status=1 GROUP BY do.teaching_age");
        return sql.toString();
    }

    public String selectSumGroupByDocumentStageSubject() {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT SUM(d.coin) AS count,do.stage AS stage,do.subject_id AS subjectId,do.subject_name AS subjectName");
        sql.append(" FROM user_order AS d LEFT JOIN document AS do ON d.data_id=do.id ");
        sql.append(" WHERE d.type=2 AND d.status=1 GROUP BY do.stage,do.subject_id");
        return sql.toString();
    }

    /**
     * 每天、每周、每月--付费会员
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
            sql.append(" (time/1000, \"%Y-%m-%d\"").append(" ) AS day , COUNT(DISTINCT(user_id)) AS count FROM user_order ");
        } else if (dayType == 2) {
            sql.append(" WEEK(FROM_UNIXTIME");
            sql.append(" (time/1000, \"%Y-%m-%d\"").append(" )) AS day ,  COUNT(DISTINCT(user_id)) AS count FROM user_order ");
        } else {
            sql.append(" MONTH(FROM_UNIXTIME");
            sql.append(" (time/1000, \"%Y-%m-%d\"").append(" )) AS day , COUNT(DISTINCT(user_id)) AS count FROM user_order ");
        }
        sql.append(" WHERE time<=").append(endTime)
                .append(" AND time>=").append(beginTime).append(" AND status=1 ");
        sql.append(" GROUP BY day ");
        return sql.toString();
    }


    public String selectSumCoinByLessTime(@Param("beginTime") Long beginTime) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT SUM(d.coin) ");
        sql.append(" FROM user_order as d");
        sql.append(" WHERE ");
//        sql.append(" d.coin IS NOT NULL AND status=1 AND type IN(2,3) ");
        sql.append(" d.coin IS NOT NULL AND status=1 AND type=2 ");
        if (beginTime != null) {
            sql.append(" AND time<=").append(beginTime);
        }
        return sql.toString();
    }

    public String selectSumRechargePriceLessTime(@Param("beginTime") Long beginTime) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT SUM(d.price) ");
        sql.append(" FROM user_order as d");
        sql.append(" WHERE ");
        sql.append(" d.price IS NOT NULL AND status=1 AND type=1 ");
        if (beginTime != null) {
            sql.append(" AND time<=").append(beginTime);
        }
        return sql.toString();
    }

    public String selectSumGroupByTypeAndTime(@Param("beginTime") Long beginTime,
                                              @Param("endTime") Long endTime,
                                              @Param("dayType") Integer dayType) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        if (dayType == 1) {
            sql.append(" FROM_UNIXTIME");
            sql.append(" (time/1000, \"%Y-%m-%d\"").append(" ) AS day , SUM(coin) AS count FROM user_order ");
        } else if (dayType == 2) {
            sql.append(" WEEK(FROM_UNIXTIME");
            sql.append(" (time/1000, \"%Y-%m-%d\"").append(" )) AS day ,  SUM(coin) AS count FROM user_order ");
        } else {
            sql.append(" MONTH(FROM_UNIXTIME");
            sql.append(" (time/1000, \"%Y-%m-%d\"").append(" )) AS day , SUM(coin) AS count FROM user_order ");
        }
        sql.append(" WHERE time<=").append(endTime)
//                .append(" AND time>=").append(beginTime).append(" AND status=1 AND type IN(2,3)");
                .append(" AND time>=").append(beginTime).append(" AND status=1 AND type =2");
        sql.append(" GROUP BY day ");
        return sql.toString();
    }


    public String selectSumPriceGroupByTypeAndTime(@Param("beginTime") Long beginTime,
                                                   @Param("endTime") Long endTime,
                                                   @Param("dayType") Integer dayType) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        if (dayType == 1) {
            sql.append(" FROM_UNIXTIME");
            sql.append(" (time/1000, \"%Y-%m-%d\"").append(" ) AS day , SUM(price) AS count FROM user_order ");
        } else if (dayType == 2) {
            sql.append(" WEEK(FROM_UNIXTIME");
            sql.append(" (time/1000, \"%Y-%m-%d\"").append(" )) AS day ,  SUM(price) AS count FROM user_order ");
        } else {
            sql.append(" MONTH(FROM_UNIXTIME");
            sql.append(" (time/1000, \"%Y-%m-%d\"").append(" )) AS day , SUM(price) AS count FROM user_order ");
        }
        sql.append(" WHERE time<=").append(endTime)
                .append(" AND time>=").append(beginTime).append(" AND status=1 AND price IS NOT NULL ");
        sql.append(" GROUP BY day ");
        return sql.toString();
    }

    public String selectSumRechargePriceByPrice(@Param("beginPrice") Double beginPrice,
                                                @Param("endPrice") Double endPrice) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT SUM(d.price) ");
        sql.append(" FROM user_order as d");
        sql.append(" WHERE ");
        sql.append(" d.price IS NOT NULL AND status=1 AND type=1 ");
        if(endPrice!=null){
            sql.append(" AND price<").append(endPrice);
        }
        if(beginPrice!=null){
            sql.append(" AND price>=").append(beginPrice);
        }
        return sql.toString();
    }

    public String selectGroupByStageGrade() {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT SUM(d.coin) AS count,do.grade AS grade,do.stage AS stage");
        sql.append(" FROM user_order AS d LEFT JOIN document AS do ON d.data_id=do.id ");
        sql.append(" WHERE d.type=2 AND d.status=1 AND do.grade IS NOT NULL GROUP BY do.grade");
        return sql.toString();
    }

}
