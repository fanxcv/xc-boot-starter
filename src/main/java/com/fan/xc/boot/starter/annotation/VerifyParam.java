package com.fan.xc.boot.starter.annotation;

import com.fan.xc.boot.starter.Dict;
import com.fan.xc.boot.starter.interfaces.ParamCheck;
import com.fan.xc.boot.starter.interfaces.ParamMap;

import java.lang.annotation.*;

/**
 * @author fan
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(VerifyParams.class)
@Documented
@Inherited
public @interface VerifyParam {

    /**
     * 使用定义好的校验类
     */
    Class<? extends ParamCheck>[] value() default {};

    /**
     * 待校验参数名
     * 配合@{@link InjectEntity}注解：
     * 可以使用“.”分割，用于对指定注入对象的值进行校验
     * 适用于同时使用多个@{@link InjectEntity}注入对象，多个对象包含重复字段，但仅部分对象字段需要校验
     */
    String[] name() default "";

    /**
     * 判断参数是否可为null
     */
    boolean required() default true;

    /**
     * 正则校验规则
     */
    String regex() default Dict.BLANK;

    /**
     * 使用Check注解标记数据校验
     */
    Check[] check() default {};

    /**
     * 参数映射器，可以在这里面预处理参数
     */
    Class<? extends ParamMap>[] map() default {};

    /**
     * 设置后，只有当required为true，并且值为null时，才会使用默认值
     */
    String defaultValue() default Dict.DEFAULT_VALUE;

    @Inherited
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.ANNOTATION_TYPE})
    @interface Check {
        Class<? extends ParamCheck> value();

        String message() default "参数错误";

        String[] args() default {};
    }
}
