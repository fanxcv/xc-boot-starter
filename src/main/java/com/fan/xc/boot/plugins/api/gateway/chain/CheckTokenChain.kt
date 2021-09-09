package com.fan.xc.boot.plugins.api.gateway.chain

import com.fan.xc.boot.plugins.adapter.redis.Redis
import com.fan.xc.boot.plugins.api.gateway.DefaultGatewayHandler.ApiCheckData
import com.fan.xc.boot.starter.Dict
import com.fan.xc.boot.starter.configuration.XcConfiguration
import com.fan.xc.boot.starter.event.Event
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.lang.NonNull
import java.util.*
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest

/**
 * 是否使用auth Token校验
 */
@ConditionalOnBean(Redis::class)
open class CheckTokenChain(private val config: XcConfiguration, private val redis: Redis) : AbstractGatewayChain<ApiCheckData>() {
    private val authKey: String by lazy {
        config.gateway?.authTokenKey ?: "IFIS_AUTH"
    }

    override fun doExec(event: Event, v: ApiCheckData): Boolean? {
        val useToken = v.check?.useToken ?: return null
        return if (useToken) {
            //其次校验Token,如果Token校验通过,直接允许
            val redisToken = getToken(v.request)
            return if (redisToken != null) {
                //通过判断redis中是否包含该token来判断Token是否过期
                redis.exists(redisToken)
            } else {
                //如果没有获取到token,继续下一步校验
                null
            }
        } else {
            null
        }
    }

    /**
     * 从cookie中获取token, 并转为redis的Key
     * @param request req
     * @return token
     */
    private fun getToken(@NonNull request: HttpServletRequest): String? {
        return Optional.ofNullable(request.cookies)
                .flatMap { array: Array<Cookie> ->
                    Arrays.stream(array) // 获取Cookie,如果有多个重名的Cookie,那随意取一个即可
                            .filter { authKey.equals(it.name, ignoreCase = true) }
                            .findAny()
                } // 获取Cookie的值
                .map { obj: Cookie -> obj.value }
                .map { Dict.AUTH_REDIS_PREFIX + authKey + it }
                .orElse(null)
    }
}