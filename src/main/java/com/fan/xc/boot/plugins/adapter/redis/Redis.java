package com.fan.xc.boot.plugins.adapter.redis;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * redis接口
 * @author fan
 */
public interface Redis {
    /**
     * 普通缓存放入
     * @param key   键
     * @param value 值
     * @return SET 在设置操作成功完成时，才返回 OK
     */
    boolean set(String key, Object value);

    /**
     * 获取redis里的值
     * @param key 待获取的key
     * @return 值
     */
    <T> T get(String key);

    /**
     * 检查给定 key 是否存在
     * @param key key
     * @return 是否存在这个key
     */
    boolean exists(String key);

    /**
     * 为key添加过期时间, 单位秒
     * @param key     key
     * @param seconds 过期时间
     * @return 设置成功返回 1 。 当 key 不存在或者不能为 key 设置过期时间时返回 0 。
     */
    boolean expire(String key, int seconds);

    /**
     * 以秒为单位，返回给定 key 的剩余生存时间(TTL, time to live)。
     * @param key key
     * @return 当 key 不存在时，返回 -2
     * 当 key 存在但没有设置剩余生存时间时，返回 -1
     * 否则，以秒为单位，返回 key 的剩余生存时间
     */
    long ttl(String key);

    /**
     * 如果Key存在,则不设置
     * @param key   key
     * @param value value
     * @return 设置成功，返回 1  设置失败，返回 0
     */
    boolean setNx(String key, String value);

    /**
     * 普通缓存放入并设置时间
     * @param key     键
     * @param value   值
     * @param seconds 时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return SET 在设置操作成功完成时，才返回 OK
     */
    boolean setEx(String key, Object value, int seconds);

    /**
     * 普通缓存放入并设置时间
     * @param key      键
     * @param value    值
     * @param time     时间值 time要大于0 如果time小于等于0 将设置无限期
     * @param timeUnit 时间单位
     * @return SET 在设置操作成功完成时，才返回 OK
     */
    boolean setEx(String key, Object value, int time, TimeUnit timeUnit);

    /**
     * 如果Key存在,则不设置,不存在的话设置key,并添加过期时间
     * @param key         key
     * @param value       value
     * @param millisecond 过期时间,毫秒
     * @return 是否设置成功
     */
    boolean setExNx(String key, Object value, long millisecond);

    /**
     * 如果Key存在,则不设置,不存在的话设置key,并添加过期时间
     * @param key      key
     * @param value    value
     * @param time     过期时间,毫秒
     * @param timeUnit 时间单位
     * @return 是否设置成功
     */
    boolean setExNx(String key, Object value, long time, TimeUnit timeUnit);

    /**
     * 递减
     * @param key key
     * @return 执行命令之后的值
     */
    long decr(String key);

    /**
     * 递增
     * @param key key
     * @return 执行命令之后的值
     */
    long incr(String key);


    /**
     * 存储map中某个属性的值
     * @param key:   key
     * @param field: map中某个field的name
     * @param value: 值
     * @return 如果字段是哈希表中的一个新建字段，并且值设置成功，返回 1 。 如果哈希表中域字段已经存在且旧值已被新值覆盖，返回 0 。
     **/
    boolean hSet(String key, String field, Object value);

    /**
     * 获取map中某个属性的值
     * @param key:   key
     * @param field: map中某个field的name
     * @return value
     **/
    <T> T hGet(String key, String field);

    /**
     * 只有在字段 field 不存在时，设置哈希表字段的值。
     * @param key:   key
     * @param field: map中某个field的name
     * @param value: 值
     * @return 设置成功，返回 1 。 如果给定字段已经存在且没有操作被执行，返回 0 。
     */
    boolean hSetNx(String key, String field, Object value);

    /**
     * 存储整个map对象
     * @param key:  key
     * @param hash: value
     * @return 如果命令执行成功，返回 OK
     **/
    boolean hmSet(String key, Map<String, Object> hash);

    /**
     * 判断缓存map中的field是否存在
     * @param key:   key
     * @param field: 需要判断的field
     * @return 判断key中是否存在这个field
     **/
    boolean hExists(String key, String field);

    /**
     * 删除Hash中的field
     * @param key   key
     * @param field field列表
     * @return 被成功删除字段的数量
     */
    long hDel(String key, String... field);

    /**
     * 通过正则获取匹配的Hash field列表
     * @param key     key
     * @param pattern 匹配正则表达式
     * @return 匹配的field Hash对象
     */
    <T> Map<String, T> hScan(String key, String pattern);

    /**
     * 获取完整Hash, 基于hScan实现
     * @param key key
     * @return Hash
     */
    <T> Map<String, T> hGetAll(String key);

    /**
     * 在list尾添加一个或多个值
     * @param key    key
     * @param string value
     * @return 执行操作后，列表的长度
     */
    long rPush(String key, Object... string);

    /**
     * 在list头添加一个或多个值
     * @param key    key
     * @param string value
     * @return 执行操作后，列表的长度
     */
    long lPush(String key, Object... string);

    /**
     * 返回列表长度
     * @param key key
     * @return 列表长度
     */
    long lLen(String key);

    /**
     * 获取列表指定范围内的元素
     * 其中 0 表示列表的第一个元素， 1 表示列表的第二个元素。 也可以使用负数下标，以 -1 表示列表的最后一个元素， -2 表示列表的倒数第二个元素
     * @param key   key
     * @param start 起始索引
     * @param stop  结束索引
     * @return 一个列表，包含指定区间内的元素
     */
    <T> List<T> lRange(String key, long start, long stop);

    /**
     * 对一个列表进行修剪(trim)，让列表只保留指定区间内的元素，不在指定区间之内的元素都将被删除。
     * 其中 0 表示列表的第一个元素， 1 表示列表的第二个元素。 也可以使用负数下标，以 -1 表示列表的最后一个元素， -2 表示列表的倒数第二个元素
     * @param key   key
     * @param start 起始索引
     * @param stop  结束索引
     * @return 命令执行成功时，返回 ok
     */
    boolean lTrim(String key, long start, long stop);

    /**
     * 移除列表的第一个元素
     * @param key key
     * @return 被移除的元素
     */
    <T> T lPop(String key);

    /**
     * 移除列表的最后一个元素
     * @param key key
     * @return 被移除的元素
     */
    <T> T rPop(String key);

    /**
     * 删除key
     * @param key 可以传一个值 或多个
     * @return 被删除 key 的数量
     */
    long del(String... key);

    /**
     * 模糊匹配删除缓存
     * @param pattern key正则
     * @return 被删除 key 的数量
     */
    long delByPattern(String pattern);

    /**
     * 通过正则获取匹配的redis key列表
     * @param pattern 正则表达式对象
     * @return key Set
     */
    Set<String> scan(String pattern);

    /**
     * 脚本执行
     * @param script 待执行脚本
     * @param clazz  返回数据类型
     * @param keys   key列表
     * @param value  值列表
     * @return 执行结果
     */
    <T> T exec(String script, Class<T> clazz, List<String> keys, Object... value);

    /**
     * 尝试获取分布式锁(单节点redis和r2m适用)
     * @param lockKey     锁的key
     * @param requestId   加锁的请求Id
     * @param millisecond 锁到期时间,单位毫秒
     * @return 是否成功获取到锁
     */
    boolean tryGetDistributedLock(String lockKey, String requestId, long millisecond);

    /**
     * 释放分布式锁(单节点redis和r2m适用)
     * @param lockKey   锁的key
     * @param requestId 加锁的请求Id
     * @return 是否成功释放锁
     */
    boolean releaseDistributedLock(String lockKey, String requestId);
}
