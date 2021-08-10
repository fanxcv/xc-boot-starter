package com.fan.xc.boot.plugins.configure;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author fan
 */
@Inherited
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({
        Configure.class,
        CustomerConfigConfiguration.class,
        CustomerConfigBeanPostProcessor.class
})
public @interface EnableCustomerConfig {
    /**
     * 用于参数缓存的配置对象
     */
    Class<? extends ConfigureCache> value() default ConfigureCache.class;
}
