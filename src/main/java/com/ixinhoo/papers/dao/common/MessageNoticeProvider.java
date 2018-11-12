package com.ixinhoo.papers.dao.common;

import com.chunecai.crumbs.code.util.collection.Collections3;
import com.google.common.base.Strings;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 文档动态sql
 *
 * @author 448778074@qq.com (cici)
 */
public class MessageNoticeProvider {


    public String selectByUserAndRoleDataCode(@Param("userCode") String userCode, @Param("roleCodes")List<String> roleCodes) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT d.* ");
        sql.append(" FROM message_notice as d");
        sql.append(" WHERE (d.type=1 ");
        if (!Strings.isNullOrEmpty(userCode)) {
            sql.append(" OR (d.type=2 AND d.data_code='");
            sql.append(userCode).append("')");
        }
        if (Collections3.isNotEmpty(roleCodes)) {
            sql.append(" OR (d.type=3 AND d.data_code IN(");
            sql.append(String.join(",", roleCodes)).append("))");
        }
        sql.append(" ) AND d.push_time<").append(System.currentTimeMillis());
        return sql.toString();
    }

}
