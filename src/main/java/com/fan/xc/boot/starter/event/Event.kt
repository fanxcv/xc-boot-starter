package com.fan.xc.boot.starter.event

import cn.hutool.core.lang.TypeReference
import com.fan.xc.boot.starter.enums.ReturnCode

interface Event {
    /**
     * 获取token
     * @return token
     */
    fun getToken(): String?

    /**
     * 返回用户IP
     * @return ip
     */
    fun getIp(): String?

    /**
     * 获取用户请求URL
     * @return url
     */
    fun getUrl(): String?

    /**
     * 返回一个参数
     * @param key 参数名
     * @return 参数值
     */
    fun getParam(key: String): Any?

    /**
     * 返回一个参数
     * @param key   参数名
     * @param clazz 参数类型
     * @return 参数值
     */
    fun <T> getParam(key: String, clazz: Class<T>): T?

    /**
     * 获取参数,基于Hutool的类型转换
     * @param key   参数名
     * @param type 参数类型 eg: new TypeReference<List<String>>() {}
     * @return 参数值
     */
    fun <T> getParam(key: String, type: TypeReference<T>): T?

    /**
     * 返回所有参数所在的集合，镜像集合
     * @return 参数集合
     */
    fun getParamMap(): MutableMap<String, Any?>

    /**
     * 把参数封装成bean后返回
     * @param clazz 待封装的Bean Class
     * @return Bean对象
     */
    fun <T> getBean(clazz: Class<T>): T?

    /**
     * 将Event对象返回为一个Map对象
     * @return 包含所有set进去的值的map
     */
    fun toMap(): MutableMap<String, Any?>

    /**
     * 设置返回值
     * @param key   返回值的key
     * @param value 返回值的value
     * @return Event对象
     */
    operator fun set(key: String, value: Any?): Event

    /**
     * 数据存入返回对象的body字段中（糖而已）
     * @param value 待存储的value
     * @return Event对象
     */
    fun setBody(value: Any?): Event

    /**
     * 解析map后放到Event对象中
     * @param value 待存入的map
     * @return Event对象
     */
    fun setMap(value: Map<out String?, *>?): Event

    /**
     * 解析Bean后放到Event对象中
     * @param value 待存入的Bean
     * @return Event对象
     */
    fun setBean(value: Any?): Event

    /**
     * 设置返回值code
     * @param code code
     * @return Event对象
     */
    fun setReturn(code: Int): Event

    /**
     * 设置返回信息
     * @param msg msg
     * @return Event对象
     */
    fun setReturn(msg: Any?): Event

    /**
     * 同时设置返回值和返回信息
     * @param code code
     * @param msg  msg
     * @return Event对象
     */
    fun setReturn(code: Int, msg: Any?): Event

    /**
     * 添加返回信息，通过枚举设置
     * @param codeEnum 返回信息
     * @return Event对象
     */
    fun setReturn(codeEnum: ReturnCode): Event

    /**
     * 设置code为成功并添加返回msg
     * @param msg msg
     * @return Event对象
     */
    fun success(msg: Any? = null): Event

    /**
     * 设置失败状态，并添加返回信息
     * @param msg msg
     * @return Event对象
     */
    fun fail(msg: Any?): Event
}