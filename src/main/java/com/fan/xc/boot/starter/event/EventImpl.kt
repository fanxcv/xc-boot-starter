package com.fan.xc.boot.starter.event

import cn.hutool.core.bean.BeanUtil
import cn.hutool.core.convert.Convert
import cn.hutool.core.lang.TypeReference
import com.google.common.collect.Maps
import com.fan.xc.boot.starter.enums.ReturnCode
import com.fan.xc.boot.starter.utils.Conversion

/**
 * 数据存储结构
 */
class EventImpl private constructor() : EventInner, HashMap<String, Any?>() {
    companion object {
        @Transient
        private val threadLocal: ThreadLocal<EventInner> = ThreadLocal()

        @Transient
        private val message = "msg"

        @Transient
        private val body = "body"

        @Transient
        private val code = "code"

        @Transient
        private val path = "path"

        @Transient
        private val time = "time"

        @JvmStatic
        fun getEvent(): EventInner = threadLocal.get() ?: instance()

        @JvmStatic
        fun removeEvent() = threadLocal.remove()

        fun instance(): EventInner {
            var event: EventInner? = threadLocal.get()
            return if (event == null) {
                event = EventImpl()
                threadLocal.set(event)
                event
            } else {
                event.init()
            }
        }
    }

    /**
     * 请求入参
     */
    @Transient
    private val params: MutableMap<String, Any?> = HashMap()

    /**
     * 请求token
     */
    @Transient
    private var token: String? = null

    /**
     * 请求地址
     */
    @Transient
    private var url: String? = null

    /**
     * 客户端IP
     */
    @Transient
    private var ip: String? = null

    /**
     * 初始化时,将code置为成功
     */
    init {
        this[code] = 0
    }

    override fun init(): EventInner {
        this.params.clear()
        this.clear()
        this[code] = 0
        this.token = null
        this.url = null
        this.ip = null
        return this
    }

    override fun putParamAll(m: Map<out String, *>?) {
        if (m != null) {
            params.putAll(m)
        }
    }

    override fun putParam(key: String, value: Any?): Any? = params.put(key, value)

    override fun setToken(token: String?) {
        this.token = token
    }

    override fun error(): MutableMap<String, Any?> {
        this[time] = System.currentTimeMillis()
        this[path] = url
        return this
    }

    override fun setUrl(url: String?) {
        this.url = url
    }

    override fun setIp(ip: String?) {
        this.ip = ip
    }

    override fun getCode(): Int = this[code] as Int

    override fun getToken(): String? = this.token

    override fun getIp(): String? = this.ip

    override fun getUrl(): String? = this.url

    override fun getParam(key: String): Any? = params[key]

    override fun <T> getParam(key: String, clazz: Class<T>): T? = Conversion.binder.convertIfNecessary(params[key], clazz)
    override fun <T> getParam(key: String, type: TypeReference<T>): T? = Convert.convert(type, params[key])

    override fun getParamMap(): MutableMap<String, Any?> = Maps.newHashMap(params)

    override fun <T> getBean(clazz: Class<T>): T? = Conversion.binder.convertIfNecessary(params, clazz)

    override fun toMap(): MutableMap<String, Any?> = this

    override fun set(key: String, value: Any?): Event {
        put(key, value)
        return this
    }

    override fun setBody(value: Any?): Event {
        this[body] = value
        return this
    }

    override fun setMap(value: Map<out String?, *>?): Event {
        value?.forEach { (k, v) ->
            if (k != null) this[k] = v
        }
        return this
    }

    override fun setBean(value: Any?): Event {
        if (value != null) {
            this.putAll(BeanUtil.beanToMap(value))
        }
        return this
    }

    override fun setReturn(code: Int): Event {
        this[Companion.code] = code
        return this
    }

    override fun setReturn(msg: Any?): Event {
        this[message] = msg
        return this
    }

    override fun setReturn(code: Int, msg: Any?): Event {
        this[Companion.code] = code
        this[message] = msg
        return this
    }

    override fun setReturn(codeEnum: ReturnCode): Event {
        this[message] = codeEnum.message()
        this[code] = codeEnum.code()
        return this
    }

    override fun success(msg: Any?): Event {
        this[message] = msg
        this[code] = 0
        return this
    }

    override fun fail(msg: Any?): Event {
        this[message] = msg
        this[code] = -1
        return this
    }
}
