package com.fan.xc.boot.plugins.weixin

import com.alibaba.fastjson.JSONObject
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.locks.ReentrantLock

abstract class AbstractTokenManager : TokenManager {
    protected val log: Logger = LoggerFactory.getLogger(this::class.java)

    /**
     * 用于实现自旋锁
     */
    private val lock: ReentrantLock = ReentrantLock()

    /**
     * 用于记录Token更新次数
     */
    protected val updateCount: AtomicInteger = AtomicInteger(Int.MIN_VALUE)

    class TokenEntity {
        /**
         * token
         */
        var token: String? = null

        /**
         * 到期时间，单位ms
         */
        var expires: Long = 0

        /**
         * 下次刷新时间，单位ms
         */
        var refresh: Long = 0

        /**
         * Token更新次数
         */
        val updateCount: AtomicInteger = AtomicInteger(Int.MIN_VALUE)
        // var lastRefreshTime: String = "01-01 00:00:00"//上次刷新时间
    }

    /**
     * Token处理,判断Token是否需要刷新
     */
    protected fun checkTokenUpdate(entity: TokenEntity) {
        lock.lock()
        try {
            // 如果两个值不等,那证明还有异步任务待执行,跳过本次执行
            if (entity.updateCount.get() == updateCount.get()) {
                val now = System.currentTimeMillis()
                //如果expires时间为0，证明还未初始化
                //如果到期时间大于当前时间，那就必须先刷新
                if (entity.expires == 0L || entity.expires <= now) {
                    updateCount.incrementAndGet()
                    refresh(entity)
                    log.info("===> {}: sync refresh finish", name())
                } else if (entity.refresh <= now) {
                    //当前时间大于刷新时间，并且在有效时间内，异步刷新即可
                    updateCount.incrementAndGet()
                    GlobalScope.launch {
                        refresh(entity)
                        log.info("===> {}: async refresh finish", name())
                    }
                }
            } else {
                log.warn("===> {}: AtomicInteger 值不相同, 不做更新操作, Global: {}, Entity: {}",
                        name(), updateCount.get(), entity.updateCount.get())
            }
        } finally {
            lock.unlock()
        }
    }

    /**
     * 更新Token相关信息
     */
    protected fun updateToken(entity: TokenEntity, json: String, key: String) {
        val time = System.currentTimeMillis()
        val map = JSONObject.parse(json) as MutableMap<*, *>
        entity.token = map[key] as String

        val expiresTime = map["expires_in"] as Int?
        if (expiresTime != null && expiresTime != 0) {
            //提前30秒就触发同步刷新
            entity.expires = time + (expiresTime - 30) * 1000L
            //提前十分钟就进行刷新操作
            entity.refresh = time + (expiresTime - 600) * 1000L
        }

        val expires = map["expires"] as Long?
        if (expires != null && expires != 0L) {
            entity.expires = expires
            entity.refresh = expires - 300 * 1000L
        }
        log.info("{} 获取到新的Token: {}, 到期时间: {}", name(), entity.token, entity.expires)
    }

    private fun refresh(entity: TokenEntity) = try {
        refreshToken()
    } finally {
        // 不管刷新结果如何,这里都必须更新刷新参数
        entity.updateCount.incrementAndGet()
    }

    /**
     * 执行token刷新的方法
     */
    protected abstract fun refreshToken()

    /**
     * 日志标志
     */
    protected abstract fun name(): String
}
