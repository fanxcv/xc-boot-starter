package com.fan.xc.boot.plugins.weixin.entity

import com.alibaba.fastjson.annotation.JSONField

class WXLoginResp : WXBaseResp() {
    /**
     * access_token
     */
    @JSONField(name = "access_token")
    var accessToken: String? = null

    /**
     * token到期时间
     */
    @JSONField(name = "expires_in")
    var expiresIn: Int? = null

    /**
     * 用户刷新access_token
     */
    @JSONField(name = "refresh_token")
    var refreshToken: String? = null

    /**
     * 用户唯一标识
     */
    var openid: String? = null

    /**
     * 用户授权的作用域，使用逗号（,）分隔
     */
    var scope: String? = null
}