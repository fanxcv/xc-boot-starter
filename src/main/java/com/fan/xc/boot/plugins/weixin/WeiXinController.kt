package com.fan.xc.boot.plugins.weixin

import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/inner/wx/")
open class WeiXinController(private val accessTokenManager: AccessTokenManager,
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
}