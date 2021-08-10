package com.fan.xc.boot.starter.event

interface EventInner : Event {
    /**
     * 存储所有的请求参数
     * @param m 待存储的键值对
     */
    fun putParamAll(m: Map<out String, *>?)

    /**
     * 向参数表中添加单个键值
     * @param key key
     * @param value value
     * @return value
     */
    fun putParam(key: String, value: Any?): Any?

    /**
     * 添加token
     * @param token token
     */
    fun setToken(token: String?)

    /**
     * 返回异常信息对象
     * @return 异常信息对象
     */
    fun error(): MutableMap<String, Any?>

    /**
     * 保存用户请求url
     * @param url url
     */
    fun setUrl(url: String?)

    /**
     * 保存用户ip
     * @param ip ip
     */
    fun setIp(ip: String?)

    /**
     * 获取返回码
     * @return code
     */
    fun getCode(): Int

    /**
     * 初始化Event对象，用于清空旧数据
     * @return Event对象
     */
    fun init(): EventInner
}