package com.fan.xc.boot.plugins.weixin

import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/inner/wx/")
open class WeiXinController(private val config: WeiXinConfig,
                            private val accessTokenManager: AccessTokenManager,
                            private val jsApiTicketManager: JsApiTicketManager) {

    @GetMapping("token")
    open fun getToken(): Map<String, Any> {
        return mapOf("jsTicket" to jsApiTicketManager.token(),
                "jsTicketExpires" to jsApiTicketManager.expires(),
                "accessToken" to accessTokenManager.token(),
                "accessTokenExpires" to accessTokenManager.expires())
    }

    @PutMapping("initLock")
    open fun initLock() {
        accessTokenManager.initLock()
        jsApiTicketManager.initLock()
    }

    @PutMapping("initToken")
    open fun initToken() {
        accessTokenManager.accessToken.expires = 0L
        jsApiTicketManager.jsApiTicket.expires = 0L
    }

    @GetMapping("syncToken/{type}/{token}")
    open fun accessToken(@PathVariable type: String, @PathVariable token: String): Map<String, Any> {
        return if (config.authorization == token) {
            when (type) {
                "accessToken" -> mapOf("code" to 0, "access_token" to accessTokenManager.token(), "expires" to accessTokenManager.expires())
                "jsTicket" -> mapOf("code" to 0, "ticket" to jsApiTicketManager.token(), "expires" to jsApiTicketManager.expires())
                else -> mapOf("code" to -999999, "msg" to "unsupported")
            }
        } else {
            mapOf("code" to -111111, "msg" to "authorization failed")
        }
    }
}