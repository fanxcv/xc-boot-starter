package com.fan.xc.boot.plugins.weixin.entity

/**
 * @author fan
 */
open class CustomMessage {
    /**
     * 消息接收人
     */
    var touser: String? = null

    /**
     * 消息类型
     */
    var msgtype: WxType? = null

}