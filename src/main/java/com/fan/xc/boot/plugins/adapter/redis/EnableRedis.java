package com.fan.xc.boot.plugins.adapter.redis;

import com.fan.xc.boot.starter.configuration.XcConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author fan
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({
        RedisConfigure.class,
        SpringRedisImpl.class,
        XcConfiguration.class
})
public @interface EnableRedis {
}
