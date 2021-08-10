package com.fan.xc.boot.plugins.api.gateway.chain

import com.fan.xc.boot.plugins.api.gateway.DefaultGatewayHandler
import com.fan.xc.boot.starter.Dict
import com.fan.xc.boot.starter.event.Event
import com.fan.xc.boot.starter.utils.Tools

/**
 * IP校验父类, 提供获取应用IP能力
 */
abstract class AbstractIpCheckChain : AbstractGatewayChain<DefaultGatewayHandler.ApiCheckData>() {

    /**
     * 计算请求IP
     * 从右侧开始, 获取第一个非内网IP, 如果全部都是内网IP, 则保留最后一个IP, 即起始第一个IP
     */
    protected fun computedIp(event: Event): String {
        val ips: List<String> = event.getIp()?.split(",") ?: listOf()
        return when {
            ips.size == 1 -> ips[0]
            ips.size > 1 -> {
                for (i in ips.size downTo 1) {
                    if (!Tools.isInnerIp(ips[i])) {
                        return ips[i]
                    }
                }
                return ips[0]
            }
            else -> Dict.BLANK
        }
    }
}