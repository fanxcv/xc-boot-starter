package com.fan.xc.boot.plugins.configure;

/**
 * 用于实现自定义参数加载
 * @author fan
 * date Create in 15:31 2020/1/7
 */
public interface LoadConfig {
    /**
     * 用于向ConfigureCache对象内添加参数
     * @param configs ConfigureCache对象
     */
    void load(ConfigureCache configs);
}
