package com.fan.xc.boot.plugins.configure;

import java.util.Map;
import java.util.UUID;

/**
 * 用于实现自定义参数加载
 * @author fan
 * date Create in 15:31 2020/1/7
 */
public interface IConfigureLoader {
    /**
     * 用于向ConfigureCache对象内添加参数
     *
     * @param configs 保存config的对象
     */
    void load(Map<String, Object> configs);

    /**
     * 配置文件名称，默认使用uuid随机生成
     * 重名会导致覆盖
     *
     * @return 名称字符串
     */
    default String name() {
        return UUID.randomUUID().toString();
    }
}
