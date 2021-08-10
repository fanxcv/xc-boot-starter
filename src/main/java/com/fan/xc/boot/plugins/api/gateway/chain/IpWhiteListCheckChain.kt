package com.fan.xc.boot.plugins.api.gateway.chain

import cn.hutool.cache.CacheUtil
import com.fan.xc.boot.plugins.api.gateway.ApiGroup
import com.fan.xc.boot.plugins.api.gateway.DefaultGatewayHandler.ApiCheckData
import com.fan.xc.boot.starter.configuration.XcConfiguration
import com.fan.xc.boot.starter.event.Event
import com.fan.xc.boot.starter.utils.Tools
import com.google.common.collect.Lists
import com.google.common.collect.Maps
import org.springframework.web.method.HandlerMethod
import java.util.*
import javax.servlet.http.HttpServletRequest

/**
 * 通过Ip校验访问权限,白名单模式
 * 按url精确控制,组合
 */
open class IpWhiteListCheckChain(private val config: XcConfiguration) : AbstractIpCheckChain() {
    companion object {
        /**
         * 接口校验缓存
         */
        private val cache = CacheUtil.newLRUCache<String, List<String>>(256)

        @JvmStatic
        fun cleanCache() = cache.clear()
    }

    private val default = "default"

    override fun doExec(handler: HandlerMethod, request: HttpServletRequest, event: Event, v: ApiCheckData?): Pair<Boolean?, ApiCheckData?> {
        val url = event.getUrl()!!
        // 先从缓存获取相关的校验配置
        val checkList: List<String> = cache[url] ?: initCheckList(handler, url)

        // 如果无法获取到任何规则匹配,返回校验失败
        if (checkList.isNotEmpty()) {
            if (v?.ip == null) {
                v?.ip = computedIp(event)
            }
            return Pair(Tools.ipIsInCidr(v?.ip, checkList), null)
        }

        return Pair(false, null)
    }

    /**
     * 计算URL的规则
     */
    @Synchronized
    private fun initCheckList(handler: HandlerMethod, url: String): List<String> {
        log.debug("init {} check list", handler.toString())
        // 每次都去配置文件对象中获取,避免刷新后获取到历史配置
        val whiteList = config.gateway?.ip?.white ?: Maps.newHashMap()
        // 默认需要添加DEFAULT的权限
        val list: MutableList<String> = Lists.newArrayList()
        Optional.ofNullable(whiteList[default]).ifPresent { list.addAll(it) }
        // 通过请求Url获取拦截规则,由于yml不支持/,所以这里需要将url中的/全部去掉
        Optional.ofNullable(whiteList[url.replace("/", "")]).ifPresent { list.addAll(it) }
        // 2.通过group查找拦截规则
        handler.getMethodAnnotation(ApiGroup::class.java)?.value
                ?.asList()?.stream()
                ?.flatMap { whiteList[it]?.stream() }
                ?.distinct()
                ?.forEach { if (it.isNotBlank()) list.add(it) }

        // 将规则缓存起来
        cache.put(url, list)

        return list
    }
}