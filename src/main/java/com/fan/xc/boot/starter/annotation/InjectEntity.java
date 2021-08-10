package com.fan.xc.boot.starter.annotation;

import java.lang.annotation.*;

/**
 * @author fan
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface InjectEntity {
    String value() default "";
}
