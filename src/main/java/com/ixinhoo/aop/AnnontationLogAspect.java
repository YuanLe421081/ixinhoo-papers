package com.ixinhoo.aop;


import com.chunecai.crumbs.code.util.aspect.AspectParamsUtil;
import com.chunecai.crumbs.code.util.http.NetworkUtil;
import com.chunecai.crumbs.code.util.mapper.JsonMapper;
import com.google.common.base.Strings;
import com.ixinhoo.annotation.ControllerLogInterface;
import com.ixinhoo.annotation.ServiceLogInterface;
import com.ixinhoo.papers.entity.EntitySetting;
import com.ixinhoo.papers.entity.authority.SystemUserOperate;
import com.ixinhoo.papers.service.authority.SystemUserOperateService;
import com.ixinhoo.shiro.PapersShiroUser;
import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Map;


/**
 * spring aop;
 * 使用注解形式;
 * 这个是需要手动注解录入信息，只需要写入注解一行字即可。
 * 如：@ControllerLogInterface(content="删除零售物流",actionType="0400",idIsObject=false,idIndex=0)
 *
 * @author 448778074@qq.com (cici)
 */
@Aspect
@Component
public class AnnontationLogAspect {
    private static Logger logger = LoggerFactory.getLogger(AnnontationLogAspect.class);
    //注入Service用于把日志保存数据库
    @Autowired
    private SystemUserOperateService service;

    //Service层切点
    @Pointcut("@annotation(com.ixinhoo.annotation.ServiceLogInterface)")
    public void serviceAspect() {
    }

    //Controller层切点
    @Pointcut("@annotation(com.ixinhoo.annotation.ControllerLogInterface)")
    public void controllerAspect() {
    }

    /**
     * 前置通知 用于拦截Controller层记录用户的操作
     *
     * @param joinPoint 切点
     */
    @Before("controllerAspect()")
    public void doControllerBefore(JoinPoint joinPoint) {
        saveLogByJoinPoint(joinPoint, null, "ControllerLog");
    }

    /**
     * 前置通知 用于拦截Service层记录用户的操作
     *
     * @param joinPoint 切点
     */
    @Before("serviceAspect()")
    public void doServiceBefore(JoinPoint joinPoint) {
        saveLogByJoinPoint(joinPoint, null, "ServiceLog");
    }

    /**
     * 后置通知;service的异常操作记录;
     *
     * @param joinPoint
     * @param e
     */
    @AfterThrowing(pointcut = "serviceAspect()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Throwable e) {
        saveLogByJoinPoint(joinPoint, e, "ServiceLog");
    }

    /**
     * 根据joinPoint,异常数据,annotationName注解名称,保存日志记录
     *
     * @param joinPoint
     * @param e
     * @param annotationName
     */
    private void saveLogByJoinPoint(JoinPoint joinPoint, Throwable e, String annotationName) {
        //读取session中的用户
        PapersShiroUser user = (PapersShiroUser) SecurityUtils.getSubject().getPrincipal();
        if (user == null) {//用户不存在,跳过
            return;
        }
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        //获取请求ip
        String ip = request == null ? "" : NetworkUtil.ipAddress(request);
        SystemUserOperate systemUserOperate = new SystemUserOperate();
        systemUserOperate.setUserId(user.getId());
        systemUserOperate.setUserName(user.getName());
        systemUserOperate.setIp(ip);
        systemUserOperate.setType(EntitySetting.OPERATE_EDIT_DATA);
        try {
            if (e != null) {
                systemUserOperate.setException(e.getMessage());
            }
            String targetName = joinPoint.getTarget().getClass().getName();
            //获取方法名
            String methodName = joinPoint.getSignature().getName();
            Object[] arguments = joinPoint.getArgs();
            systemUserOperate.setMethodName(methodName);
            systemUserOperate.setClassLocation(targetName);
            //获取操作内容
            systemUserOperate.setContent(AspectParamsUtil.paramsOptionContent(arguments));
            Class targetClass = Class.forName(targetName);
            Method[] method = targetClass.getMethods();
            systemUserOperate.setEntityName(targetName);
            for (Method m : method) {
                if (m.getName().equals(methodName)) {
                    Class[] tmpCs = m.getParameterTypes();
                    if (tmpCs.length == arguments.length) {
                        if ("ServiceLog".equals(annotationName)) {
                            ServiceLogInterface methodLog = m.getAnnotation(ServiceLogInterface.class);
//                            systemUserOperate.setActionType(methodLog.actionType());
                            String id = paramsId(arguments, methodLog.idIndex(), methodLog.idIsObject());
                            if (Strings.isNullOrEmpty(id)) {
                                systemUserOperate.setRemark(methodLog.content());
                            } else {
                                systemUserOperate.setRemark(methodLog.content() + "(id:" + id + ")");
                            }
//                            systemUserOperate.setInterfaceType(methodLog.interfaceType());
                        } else {
                            ControllerLogInterface methodLog = m.getAnnotation(ControllerLogInterface.class);
//                            systemUserOperate.setActionType(methodLog.actionType());
                            String id = paramsId(arguments, methodLog.idIndex(), methodLog.idIsObject());
                            if (Strings.isNullOrEmpty(id)) {
                                systemUserOperate.setRemark(methodLog.content());
                            } else {
                                systemUserOperate.setRemark(methodLog.content() + "(id:" + id + ")");
                            }
//                            ccLogger.setInterfaceType(methodLog.interfaceType());
                        }
                        break;
                    }
                }
            }
        } catch (Exception e2) {
            logger.error("日志保存执行出现异常:{}", e2);
            systemUserOperate.setException("日志异常" + e2.getMessage());
//            systemUserOperate.setInterfaceType("后台接口");
            systemUserOperate.setMethodName("doAfterThrowing");
            systemUserOperate.setRemark("日志异常");
//            systemUserOperate.setActionType("异常");
        } finally {
            service.loggerInsert(systemUserOperate);
        }
    }


    /**
     * 将参数中的对象所在位置id的值进行返回;
     * 获取id所在的参数位置下标,id是否为对象,
     *
     * @param args         请求参数对象
     * @param idIndex      id所在位置
     * @param isJsonObject id所在位置是否为对象
     * @return
     * @throws Exception
     */
    private String paramsId(Object[] args, int idIndex, boolean isJsonObject) {
        StringBuffer sbf = new StringBuffer();
        try {
            if (args != null && args.length != 0) {
                if (idIndex < args.length) {
                    Object info = args[idIndex];
                    if (info != null) {
                        if (isJsonObject) {
                            Map<String, Object> map = JsonMapper.nonDefaultMapper().fromJson(JsonMapper.nonDefaultMapper().toJson(info), Map.class);
                            if (map != null && map.containsKey("id")) {
                                sbf.append(map.get("id"));
                            }
                        } else {
                            sbf.append(info);
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("获取paramsId异常:{}", e);
        }
        return sbf.toString();
    }


}
