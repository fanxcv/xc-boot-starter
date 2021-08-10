package com.fan.xc.boot.plugins.swagger;

import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.lang.annotation.*;

/**
 * @author fan
 */
@Inherited
@Documented
@EnableSwagger2
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({Swagger2ImportBeanDefinitionRegistrar.class})
public @interface EnableXcSwagger {

    @AliasFor("basePackage")
    String value() default "";

    /**
     * swagger默认扫描包名
     */
    @AliasFor("value")
    String basePackage() default "";
}
