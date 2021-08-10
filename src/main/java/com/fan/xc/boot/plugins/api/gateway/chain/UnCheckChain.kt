package com.fan.xc.boot.plugins.api.gateway.chain

import com.fan.xc.boot.plugins.api.gateway.DefaultGatewayHandler.ApiCheckData
import com.fan.xc.boot.starter.event.Event
import org.springframework.web.method.HandlerMethod
import javax.servlet.http.HttpServletRequest

/**
 * 不做校验
 */
open class UnCheckChain : AbstractGatewayChain<ApiCheckData>() {
    override fun doExec(handler: HandlerMethod, request: HttpServletRequest, event: Event, v: ApiCheckData?): Pair<Boolean?, ApiCheckData?> {
        // 如果用户设置了unCheck属性,那就直接允许访问
        return if (v?.check?.unCheck == true) Pair(true, null) else Pair(null, v)
    }
}