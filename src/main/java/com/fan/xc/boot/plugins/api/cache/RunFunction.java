package com.fan.xc.boot.plugins.api.cache;

/**
 * @author fan
 */
@FunctionalInterface
public interface RunFunction {
    /**
     * 待执行的方法
     * @return 执行结果
     * @throws Throwable 执行异常
     */
    Object run() throws Throwable;
}
