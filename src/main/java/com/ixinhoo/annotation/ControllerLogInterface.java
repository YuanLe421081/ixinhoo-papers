package com.ixinhoo.annotation;

import java.lang.annotation.*;

/**
 * 控制层的自定义注解;
 *
 * @author 448778074@qq.com (cici)
 */
@Target({ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ControllerLogInterface {
    /**
     * 控制层进入的动作,主要是干嘛的;比如:打开用户新增页面;保存用户信息;
     *
     * @return
     */
    String content() default "";

    /**
     * 控制层进入进行的动作,默认是查询;如:动作类型,如:新增,编辑,删除,查询等
     * 0100,0200,0300,0400
     *
     * @return
     */
    String actionType() default "0400";

    /**
     * 控制层进入处理的接口,如:前端接口,后台接口;
     * 前端接口一般处理的是手机端访问的接口,后台接口处理的是后台的页面信息;
     *
     * @return
     */
    String interfaceType() default "后台接口";

    /**
     * 获取id所在的参数位置下标,默认为0;下标从0开始
     *
     * @return
     */
    int idIndex() default 0;

    /**
     * id是否为对象,默认为非对象,id所在的位置如果在对象中,注意对象可以进行json转换;
     *
     * @return
     */
    boolean idIsObject() default false;
}
