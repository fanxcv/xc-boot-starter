package com.fan.xc.boot.plugins.api.gateway.chain

import com.fan.xc.boot.plugins.adapter.redis.Redis
import com.fan.xc.boot.plugins.api.gateway.DefaultGatewayHandler.ApiCheckData
import com.fan.xc.boot.starter.event.Event
import com.fan.xc.boot.starter.exception.XcRunException
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean

/**
 * 接口间隔时间限制
 */
@ConditionalOnBean(Redis::class)
open class TimeLimitChain(private val redis: Redis) : AbstractGatewayChain<ApiCheckData>() {
    override fun doExec(event: Event, v: ApiCheckData): Boolean? {
        val limit = v.check?.timeLimit ?: return null
        if (limit.value > 0 && !redis.setExNx(event.getUrl(), null, limit.value, limit.time)) {
            throw XcRunException(-103, "请求频率过高,请稍后再试")
        }
        return null
    }
}