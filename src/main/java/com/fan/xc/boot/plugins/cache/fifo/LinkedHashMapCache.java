package com.fan.xc.boot.plugins.cache.fifo;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 基于LinkedHashMap实现的LRU
 * @author fan
 */
public class LinkedHashMapCache<K, V> extends LinkedHashMap<K, V> {
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private final int initialCapacity;

    /**
     * 初始化大小
     * @param initialCapacity 初始值大小
     */
    public LinkedHashMapCache(int initialCapacity) {
        // 当参数accessOrder为true时，即会按照访问顺序排序，最近访问的放在最前，最早访问的放在后面
        super(initialCapacity, DEFAULT_LOAD_FACTOR, true);
        this.initialCapacity = initialCapacity;
    }

    /**
     * 是否移除最旧的数据
     * @param eldest 最旧的节点
     * @return 是否移除最旧的数据
     */
    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return this.size() > initialCapacity;
    }
}
