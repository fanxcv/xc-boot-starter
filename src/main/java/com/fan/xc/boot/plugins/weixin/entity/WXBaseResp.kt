package com.fan.xc.boot.plugins.weixin.entity

open class WXBaseResp {
    /**
     * 微信返回码
     */
    var errcode: Int? = null

    /**
     * 微信返回信息
     */
    var errmsg: String? = null
}