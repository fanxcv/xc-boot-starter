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
@Import({ConfigureEventSender.class, ConfigureConfiguration.class})
public @interface EnableCustomerConfig {
}
