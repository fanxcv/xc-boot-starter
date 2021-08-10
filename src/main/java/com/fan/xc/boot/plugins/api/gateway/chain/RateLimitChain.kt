package com.fan.xc.boot.plugins.api.gateway.chain

import com.fan.xc.boot.plugins.api.gateway.DefaultGatewayHandler.ApiCheckData
import com.fan.xc.boot.starter.configuration.XcConfiguration
import com.fan.xc.boot.starter.event.Event
import com.fan.xc.boot.starter.exception.XcRunException
import com.google.common.collect.Maps
import com.google.common.util.concurrent.RateLimiter
import org.springframework.web.method.HandlerMethod
import java.util.concurrent.ConcurrentMap
import java.util.concurrent.TimeUnit
import javax.servlet.http.HttpServletRequest

/**
 * 接口限流, 基于令牌桶
 * 如果配置了全局限流, 则所有接口未指定独立限流的接口共用同一个限流器
 */
open class RateLimitChain(private val config: XcConfiguration) : AbstractGatewayChain<ApiCheckData>() {
    private val map: ConcurrentMap<HandlerMethod, RateLimiter> = Maps.newConcurrentMap()

    // 如果xc.gateway.rateLimit 值大于 0, 全局限流器生效
    private val usePublicLimiter: Boolean by lazy {
        (config.gateway?.rateLimit ?: -1) > 0
    }

    // 接口等待时间, 默认等待1秒
    private val timeout: Long by lazy {
        config.gateway?.rateTimeout ?: 1
    }

    // 公共限流器, 默认流速256
    private val publicRateLimiter: RateLimiter by lazy {
        val limit: Double = config.gateway?.rateLimit?.toDouble() ?: 256.0
        RateLimiter.create(limit)
    }

    override fun doExec(handler: HandlerMethod, request: HttpServletRequest, event: Event, v: ApiCheckData?): Pair<Boolean?, ApiCheckData?> {
        val limit = v?.check?.rateLimit ?: -1L
        // 如果limit的值大于0,证明需要进行限流
        if (limit > 0) {
            val limiter: RateLimiter = map[handler] ?: initLimiter(handler, limit)
            if (!limiter.tryAcquire()) {
                throw XcRunException(-101, "请求频率过高,请稍后再试")
            }
        } else if (usePublicLimiter && !publicRateLimiter.tryAcquire(timeout, TimeUnit.SECONDS)) {
            throw XcRunException(-102, "请求频率过高,请稍后再试")
        }
        return Pair(null, v)
    }

    @Synchronized
    private fun initLimiter(handler: HandlerMethod, limit: Long): RateLimiter {
        val limiter = RateLimiter.create(limit.toDouble())
        map[handler] = limiter
        return limiter
    }
}