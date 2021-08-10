package com.fan.xc.boot.plugins.api.cache;

import com.fan.xc.boot.starter.Dict;

import java.lang.annotation.*;

/**
 * @author fan
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface XCCache {
    /**
     * 缓存Key
     */
    String value();

    /**
     * 判断是否返回新数据的key，用于前端自带缓存的业务
     */
    String checkKey() default Dict.DEFAULT_VALUE;

    /**
     * 前端传过来的检查值，用于前端自带缓存的业务
     */
    String checkValue() default Dict.DEFAULT_VALUE;

    /**
     * 数据最大缓存时间, 默认两小时过期
     */
    int expire() default 7200;

    /**
     * 提前异步刷新时间, 默认-1, 不异步刷新
     * 当值大于0时, 会触发异步刷新机制
     */
    int refreshBefore() default -1;

    /**
     * 是否续期过期时间
     */
    boolean renewal() default true;

    /**
     * 需要缓存的数据列表，会从Event对象中获取
     */
    String[] cacheKeys() default {};
}
