package com.fan.xc.boot.plugins.weixin

import com.fan.xc.boot.plugins.weixin.grpc.WeiXinRpcClient
import com.fan.xc.boot.starter.exception.XcRunException
import com.fan.xc.boot.starter.utils.NetUtils
import org.springframework.context.annotation.Lazy

@Lazy
class AccessTokenManager(private val config: WeiXinConfig,
                         private val weiXinRpcClient: WeiXinRpcClient) : AbstractTokenManager() {
    val accessToken = TokenEntity()

    override fun token(): String {
        checkTokenUpdate(accessToken)
        return accessToken.token ?: throw XcRunException("get WeiXin accessToken fail")
    }

    override fun expires(): Long {
        return accessToken.expires
    }

    override fun initLock() {
        accessToken.updateCount.set(Int.MIN_VALUE)
        super.updateCount.set(Int.MIN_VALUE)
        log.debug("JsApiTicketManager: initLock, Global: {}, Entity: {}", updateCount.get(), accessToken.updateCount.get())
    }

    override fun refreshToken() {
        log.info("===> refresh WeiXin accessToken")
        val json = if (config.client?.isEnable == true) weiXinRpcClient.getAccessToken()
        else NetUtils.get("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=${config.appId}&secret=${config.appSecret}")
        //判断是否正确获取到accessToken了
        if (json.contains("\"access_token\"")) {
            updateToken(accessToken, json, "access_token")
        } else {
            log.error("refresh WeiXin accessToken fail, {}", json)
        }
    }

    override fun name() = "AccessTokenManager"
}