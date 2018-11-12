package com.ixinhoo.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

/**
 * 移动设备认证基类，提供未登录用户操作认证权限
 *
 * @author 448778074@qq.com (cici)
 */
public class MobileAuthenticatingFilter extends AuthenticatingFilter {
    @Override
    protected AuthenticationToken createToken(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        return null;
    }

    /**
     * 针对手机端用户访问的api/v1的接口进行没有权限的认证返回
     *
     * @param servletRequest  请求
     * @param servletResponse 相应
     * @return 状态
     * @throws Exception 异常
     */
    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        Subject s = SecurityUtils.getSubject();
        boolean flag = true;
        if (!s.isAuthenticated()) {
            HttpServletResponse response = (HttpServletResponse) servletResponse;
            response.setContentType("application/json;charset=utf-8");
            response.setCharacterEncoding("utf-8");
//            response.setStatus(302);
//            StatusDto status = new StatusDto(false);
//            status.setCode("302");
//            status.setMsg("用户未登录");
//            String s1 = JsonMapper.nonDefaultMapper().toJson(status);
//            System.out.println(s1);
            response.getWriter().append("{\"status\":false,\"code\":302,\"msg\":\"用户未登录\"}").flush();
            flag = false;
        }
        return flag;
    }
}