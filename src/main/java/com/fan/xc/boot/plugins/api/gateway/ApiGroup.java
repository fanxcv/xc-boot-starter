package com.fan.xc.boot.plugins.api.gateway;

import java.lang.annotation.*;

/**
 * 为Api添加分组,后面可以通过为该组设置规则来控制该组下下的所有接口权限
 * @author fan
 */
@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface ApiGroup {
    /**
     * group名字
     */
    String[] value();
}
