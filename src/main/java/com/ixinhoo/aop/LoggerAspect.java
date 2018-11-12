package com.ixinhoo.aop;

import com.chunecai.crumbs.api.util.UsersUtil;
import com.chunecai.crumbs.code.util.aspect.AspectParamsUtil;
import com.chunecai.crumbs.code.util.http.NetworkUtil;
import com.ixinhoo.papers.entity.EntitySetting;
import com.ixinhoo.papers.entity.authority.SystemUserOperate;
import com.ixinhoo.papers.service.authority.SystemUserOperateService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 管理员操作日志-aop 切面;
 *
 * @author 448778074@qq.com (cici)
 */
@Aspect
@Component
public class LoggerAspect {
    private Logger logger = LoggerFactory.getLogger(LoggerAspect.class);
    /**
     * 日志记录Service
     */
    @Autowired
    private SystemUserOperateService service;

    /**
     * 添加业务逻辑方法切入点
     */
    @Pointcut("execution(* com.ixinhoo.papers.service..*.create*(..)) || execution(* com.chunecai.crumbs.api.service.BaseService+.create*(..))")
    public void insertServiceCall() {
    }

    /**
     * 修改业务逻辑方法切入点
     */
    @Pointcut("execution(* com.ixinhoo.papers.service..*.save*(..)) || execution(* com.chunecai.crumbs.api.service.BaseService+.update*(..))")
    public void updateServiceCall() {
    }

    /**
     * 删除业务逻辑方法切入点
     */
    @Pointcut("execution(* com.ixinhoo.papers.service..*.delete*(..)) || execution(* com.chunecai.crumbs.api.service.BaseService+.delete*(..))")
    public void deleteServiceCall() {
    }


//    /**
//     * 查询业务逻辑方法切入点
//     */
//    @Pointcut("execution(* com.fujj.**.service.*.*.delete*(..))")
//    public void selectServiceCall() {
//    }

    /**
     * 管理员添加操作日志(后置通知)
     *
     * @param joinPoint
     * @param rtv
     * @throws Throwable
     */
    @AfterReturning(value = "insertServiceCall()", returning = "rtv")
    public void insertServiceCallCalls(JoinPoint joinPoint, Object rtv) {
        //获取登录管理员id
//        String adminUserId = UsersUtil.id();
//        if (adminUserId == null) {//没有管理员登录
//            return;
//        }
//        //判断参数
//        if (joinPoint.getArgs() == null) {//没有参数
//            return;
//        }
        databaseUpdate(joinPoint, EntitySetting.OPERATE_EDIT_DATA);
    }

    /**
     * 管理员删除操作日志(后置通知)
     *
     * @param joinPoint
     * @param rtv
     * @throws Throwable
     */
    @AfterReturning(value = "deleteServiceCall()", returning = "rtv")
    public void deleteServiceCallCalls(JoinPoint joinPoint, Object rtv) {
        //获取登录管理员id
//        String adminUserId = UsersUtil.id();
//        if (adminUserId == null) {//没有管理员登录
//            return;
//        }
//        //判断参数
//        if (joinPoint.getArgs() == null) {//没有参数
//            return;
//        }
        databaseUpdate(joinPoint, EntitySetting.OPERATE_EDIT_DATA);
    }

    /**
     * 管理员更新操作日志(后置通知)
     *
     * @param joinPoint
     * @param rtv
     * @throws Throwable
     */
    @AfterReturning(value = "updateServiceCall()", returning = "rtv")
    public void updateServiceCallCalls(JoinPoint joinPoint, Object rtv) {
//        //获取登录管理员id
//        String adminUserId = UsersUtil.id();
//        if (adminUserId == null) {//没有管理员登录
//            return;
//        }
//        //判断参数
//        if (joinPoint.getArgs() == null) {//没有参数
//            return;
//        }
        databaseUpdate(joinPoint, EntitySetting.OPERATE_EDIT_DATA);
    }

    /**
     * 更新数据库;就算日志记录失败,也不影响正常的数据运行
     *
     * @param joinPoint
     */
    private void databaseUpdate(JoinPoint joinPoint, Integer type) {
        try {
            //获取操作内容
//            String opContent = LoggerAspectParamsUtil.adminOptionContent(joinPoint.getArgs(), type.name());
            SystemUserOperate systemUserOperate = new SystemUserOperate();
            systemUserOperate.setUserName(UsersUtil.name());//设置管理员名称
            systemUserOperate.setType(type);//操作
//            ccLogger.setRemark(opContent);//操作内容
            systemUserOperate.setRemark("修改数据");//操作内容
            systemUserOperate.setEntityName(joinPoint.toShortString());
//            systemUserOperate.setInterfaceType("后台接口");
            String targetName = joinPoint.getTarget().getClass().getName();
            //获取方法名
            String methodName = joinPoint.getSignature().getName();
            systemUserOperate.setContent(AspectParamsUtil.paramsOptionContent(joinPoint.getArgs()));
            systemUserOperate.setMethodName(methodName);
            systemUserOperate.setClassLocation(targetName);
            systemUserOperate.setUserId(UsersUtil.id());
            // 用户IP地址
            if ((RequestContextHolder.getRequestAttributes()) != null) {
                HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
                if (request != null) {
                    systemUserOperate.setIp(NetworkUtil.ipAddress(request));
                }
            }
            service.loggerInsert(systemUserOperate);//添加日志
        } catch (Exception e) {
            logger.error("日志记录失败:{}", e);
        }
    }


}
