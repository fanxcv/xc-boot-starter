package com.fan.xc.boot.plugins.coroutines;

/**
 * @author fan
 */
@FunctionalInterface
public interface RunFunction {
    /**
     * 待执行的方法
     */
    void run() throws Throwable;
}
