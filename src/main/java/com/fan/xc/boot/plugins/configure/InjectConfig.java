package com.fan.xc.boot.plugins.configure;

import com.fan.xc.boot.starter.Dict;
import com.fan.xc.boot.starter.interfaces.ParamMap;

import java.lang.annotation.*;

/**
 * @author fan
 * date Create in 11:01 2020/1/8
 */
@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.FIELD})
public @interface InjectConfig {
    /**
     * 需要获取的参数名
     */
    String value() default "";

    /**
     * 参数映射器，可以在这里面预处理参数
     */
    Class<? extends ParamMap>[] map() default {};

    /**
     * 获取失败后填充的默认值
     */
    String defaultValue() default Dict.DEFAULT_VALUE;
}
