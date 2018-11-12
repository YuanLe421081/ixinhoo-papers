package com.ixinhoo.papers.dao.user;

import org.apache.ibatis.annotations.Param;

/**
 * 文档动态sql
 *
 * @author 448778074@qq.com (cici)
 */
public class UserLoginRecordDaoProvider {

    /**
     * 每天、每周、每月--活跃会员
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
            sql.append(" (time/1000, \"%Y-%m-%d\"").append(" ) AS day , COUNT(DISTINCT(user_id)) AS count FROM user_login_record ");
        } else if (dayType == 2) {
            sql.append(" WEEK(FROM_UNIXTIME");
            sql.append(" (time/1000, \"%Y-%m-%d\"").append(" )) AS day ,  COUNT(DISTINCT(user_id)) AS count FROM user_login_record ");
        } else {
            sql.append(" MONTH(FROM_UNIXTIME");
            sql.append(" (time/1000, \"%Y-%m-%d\"").append(" )) AS day , COUNT(DISTINCT(user_id)) AS count FROM user_login_record ");
        }
        sql.append(" WHERE time<=").append(endTime)
                .append(" AND time>=").append(beginTime).append(" ");
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
        sql.append("SELECT  COUNT(DISTINCT(user_id)) AS count,FROM_UNIXTIME(time/1000,'%H') AS day from user_login_record ");
        if(beginTime!=null||endTime!=null){
            sql.append(" WHERE ");
            if(endTime!=null){
                sql.append(" time<=").append(endTime);
            }
            if(beginTime!=null){
                if(endTime!=null){
                    sql.append(" AND ");
                }
                sql.append(" time>=").append(beginTime);
            }
        }
        sql.append(" GROUP BY day ");
        return sql.toString();
    }


}
