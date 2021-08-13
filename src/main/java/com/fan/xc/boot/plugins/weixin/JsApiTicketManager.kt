package com.fan.xc.boot.plugins.weixin

import com.fan.xc.boot.plugins.weixin.grpc.WeiXinRpcClient
import com.fan.xc.boot.starter.exception.XcRunException
import com.fan.xc.boot.starter.utils.NetUtils
import org.springframework.context.annotation.Lazy

@Lazy
class JsApiTicketManager(private val config: WeiXinConfig,
                         private val weiXinRpcClient: WeiXinRpcClient,
                         private val accessTokenManager: TokenManager) : AbstractTokenManager() {
    val jsApiTicket = TokenEntity()

    override fun token(): String {
        checkTokenUpdate(jsApiTicket)
        return jsApiTicket.token ?: throw XcRunException("get WeiXin jsApiTicket fail")
    }

    override fun expires(): Long {
        return jsApiTicket.expires
    }

    override fun initLock() {
        jsApiTicket.updateCount.set(Int.MIN_VALUE)
        super.updateCount.set(Int.MIN_VALUE)
        log.debug("JsApiTicketManager: initLock, Global: {}, Entity: {}", updateCount.get(), jsApiTicket.updateCount.get())
    }

    override fun refreshToken() {
        log.info("===> refresh WeiXin jsApiTicket")
        val json = if (config.client?.isEnable == true) weiXinRpcClient.getAccessToken()
        else NetUtils.get("https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=${accessTokenManager.token()}&type=jsapi")
        //判断是否正确获取到ticket了
        if (json.contains("\"ticket\"")) {
            updateToken(jsApiTicket, json, "ticket")
        } else {
            log.error("refresh WeiXin jsApiTicket fail, {}", json)
        }
    }

    override fun name() = "JsApiTicketManager"
}