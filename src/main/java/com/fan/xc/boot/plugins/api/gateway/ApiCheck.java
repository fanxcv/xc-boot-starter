package com.fan.xc.boot.plugins.api.gateway;

import java.lang.annotation.*;

/**
 * @author fan
 */
@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface ApiCheck {
    /**
     * 请求时间间隔设置
     */
    int timeLimit() default -1;

    /**
     * 请求频率限制
     */
    long rateLimit() default -1L;

    /**
     * 是否对接口不做访问限制
     */
    boolean unCheck() default false;

    /**
     * 是否可以使用Token校验访问
     * 默认关闭
     */
    boolean useToken() default false;
}
