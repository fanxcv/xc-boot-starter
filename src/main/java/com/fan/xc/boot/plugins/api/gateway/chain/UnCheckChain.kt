package com.fan.xc.boot.plugins.api.gateway.chain

import com.fan.xc.boot.plugins.api.gateway.DefaultGatewayHandler.ApiCheckData
import com.fan.xc.boot.starter.event.Event

/**
 * 不做校验
 */
open class UnCheckChain : AbstractGatewayChain<ApiCheckData>() {
    override fun doExec(event: Event, v: ApiCheckData): Boolean? {
        // 如果用户设置了unCheck属性,那就直接允许访问
        return if (v.check?.unCheck == true) true else null
    }
}