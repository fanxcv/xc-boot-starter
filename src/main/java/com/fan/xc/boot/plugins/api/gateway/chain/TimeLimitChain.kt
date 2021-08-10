package com.fan.xc.boot.plugins.api.gateway.chain

import com.fan.xc.boot.plugins.adapter.redis.Redis
import com.fan.xc.boot.plugins.api.gateway.DefaultGatewayHandler.ApiCheckData
import com.fan.xc.boot.starter.event.Event
import com.fan.xc.boot.starter.exception.XcRunException
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.web.method.HandlerMethod
import javax.servlet.http.HttpServletRequest

/**
 * 接口间隔时间限制
 */
@ConditionalOnBean(Redis::class)
open class TimeLimitChain(private val redis: Redis) : AbstractGatewayChain<ApiCheckData>() {
    override fun doExec(handler: HandlerMethod, request: HttpServletRequest, event: Event, v: ApiCheckData?): Pair<Boolean?, ApiCheckData?> {
        val limit = v?.check?.timeLimit ?: return Pair(null, v)
        if (limit > 0) {
            if (!redis.setExNx(event.getUrl(), null, limit * 1000L)) {
                throw XcRunException(-103, "请求频率过高,请稍后再试")
            }
        }
        return Pair(null, v)
    }
}