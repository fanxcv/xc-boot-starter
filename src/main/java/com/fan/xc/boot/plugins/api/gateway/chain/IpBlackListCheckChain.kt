package com.fan.xc.boot.plugins.api.gateway.chain

import com.fan.xc.boot.plugins.api.gateway.DefaultGatewayHandler.ApiCheckData
import com.fan.xc.boot.starter.configuration.XcConfiguration
import com.fan.xc.boot.starter.event.Event
import com.fan.xc.boot.starter.utils.Tools

/**
 * 通过Ip校验访问权限,黑名单模式
 */
open class IpBlackListCheckChain(private val config: XcConfiguration) : AbstractIpCheckChain() {

    override fun doExec(event: Event, v: ApiCheckData): Boolean? {
        val blackList = config.gateway?.ip?.black
        // 如果有黑名单配置才进行黑名单校验
        if (blackList?.isNotEmpty() == true) {
            // 判断下Ip是不是已经计算过了
            if (v.ip == null) {
                v.ip = computedIp(event)
            }
            // 如果IP在黑名单列表里, 返回失败, 否则继续下步校验
            if (Tools.ipIsInCidr(v.ip, blackList)) {
                return false
            }
        }
        return null
    }
}