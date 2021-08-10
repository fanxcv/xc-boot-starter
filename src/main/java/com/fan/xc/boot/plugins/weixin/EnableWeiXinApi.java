package com.fan.xc.boot.plugins.weixin;

import com.fan.xc.boot.starter.configuration.XcConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * @author fan
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({
        WeiXinRegistrar.class,
        WeiXinController.class,
        WeiXinApi.class,
        XcConfiguration.class
})
@EnableConfigurationProperties(WeiXinConfig.class)
public @interface EnableWeiXinApi {
    /**
     * 是否通过URL同步token
     */
    @AliasFor("fromApi")
    boolean value() default false;

    /**
     * 是否通过URL同步token
     */
    @AliasFor("value")
    boolean fromApi() default false;
}
