package com.fan.xc.boot.plugins.api.cache;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 开启XCCache缓存支持
 * @author fan
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({CacheUtils.class, CacheAop.class})
public @interface EnableXCCache {
}
