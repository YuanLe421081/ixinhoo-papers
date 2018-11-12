package com.ixinhoo.papers.dao.user;

import org.apache.ibatis.annotations.Param;

/**
 * 文档动态sql
 *
 * @author 448778074@qq.com (cici)
 */
public class UserDaoProvider {

    /**
     * 每天、每周、每月--新增会员
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
            sql.append(" (register_time/1000, \"%Y-%m-%d\"").append(" ) AS day , count(id) AS count FROM user ");
        } else if (dayType == 2) {
            sql.append(" WEEK(FROM_UNIXTIME");
            sql.append(" (register_time/1000, \"%Y-%m-%d\"").append(" )) AS day , count(id) AS count FROM user ");
        } else {
            sql.append(" MONTH(FROM_UNIXTIME");
            sql.append(" (register_time/1000, \"%Y-%m-%d\"").append(" )) AS day , count(id) AS count FROM user ");
        }
        sql.append(" WHERE register_time<=").append(endTime)
                .append(" AND register_time>=").append(beginTime).append(" ");
        sql.append(" GROUP BY day ");
        return sql.toString();
    }

    /**
     * 每个小时
     *
     * @param beginTime 开始时间
     * @param endTime   结束时间
     * @return
     */
    public String selectGroupByHourAndTime(@Param("beginTime") Long beginTime,
                                           @Param("endTime") Long endTime) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(id) AS count,FROM_UNIXTIME(register_time/1000,'%H') AS day from user ");
        if (beginTime != null || endTime != null) {
            sql.append(" WHERE ");
            if (endTime != null) {
                sql.append(" register_time<=").append(endTime);
            }
            if (beginTime != null) {
                if (endTime != null) {
                    sql.append(" AND ");
                }
                sql.append(" register_time>=").append(beginTime);
            }
        }
        sql.append(" GROUP BY day ");
        return sql.toString();
    }

    /**
     * 每个小时--付费
     *
     * @param beginTime 开始时间
     * @param endTime   结束时间
     * @return
     */
    public String selectGroupByHourAndFirstPayTime(@Param("beginTime") Long beginTime,
                                                   @Param("endTime") Long endTime) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(id) AS count,FROM_UNIXTIME(first_pay_time/1000,'%H') AS day from user ");
        if (beginTime != null || endTime != null) {
            sql.append(" WHERE ");
            if (endTime != null) {
                sql.append(" first_pay_time<=").append(endTime);
            }
            if (beginTime != null) {
                if (endTime != null) {
                    sql.append(" AND ");
                }
                sql.append(" first_pay_time>=").append(beginTime);
            }
        }
        sql.append(" GROUP BY day ");
        return sql.toString();
    }


}
