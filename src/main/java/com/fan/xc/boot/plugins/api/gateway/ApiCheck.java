package com.fan.xc.boot.plugins.api.gateway;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * @author fan
 */
@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface ApiCheck {

    /**
     * 请求间隔配置
     */
    TimeLimit timeLimit() default @ApiCheck.TimeLimit;

    /**
     * 请求频率配置
     */
    RateLimit[] rateLimit() default {};

    /**
     * 滑动窗口限流配置
     */
    RollLimit[] rollLimit() default {};

    /**
     * 是否对接口不做访问限制
     */
    boolean unCheck() default false;

    /**
     * 是否可以使用Token校验访问
     * 默认关闭
     */
    boolean useToken() default false;

    @Target({})
    @Retention(RetentionPolicy.RUNTIME)
    @interface TimeLimit {
        /**
         * 等待时间
         */
        long value() default -1;

        /**
         * 等待时间单位
         */
        TimeUnit time() default TimeUnit.SECONDS;
    }

    @Target({})
    @Retention(RetentionPolicy.RUNTIME)
    @interface RateLimit {
        /**
         * 周期允许次数
         */
        long value() default -1;

        /**
         * 时间周期
         */
        long time() default 1;

        /**
         * 周期时间单位
         */
        TimeUnit timeUnit() default TimeUnit.SECONDS;
    }

    @Target({})
    @Retention(RetentionPolicy.RUNTIME)
    @interface RollLimit {
        /**
         * 请求次数
         */
        long value() default -1;

        /**
         * 窗口个数
         */
        long window() default 10;

        /**
         * 时间
         */
        long time() default 1;

        /**
         * 时间单位
         */
        TimeUnit timeUnit() default TimeUnit.SECONDS;

        /**
         * 缓存Key,支持EL表达式,可以从入参中获取值#{#param}
         */
        String key();
    }
}
