package com.fan.xc.boot.plugins.weixin.entity

enum class WxType(private val v: String) {
    /**
     * 文本消息
     */
    TEXT("text"),

    /**
     * 图片消息
     */
    IMAGE("image");

    override fun toString(): String = v
}