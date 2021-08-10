package com.fan.xc.boot.plugins.weixin.entity

import com.alibaba.fastjson.annotation.JSONField

/**
 * @author fan
 */
class TemplateMessage {

    class MiniProgram {
        /**
         * 所需跳转到的小程序appid（该小程序appid必须与发模板消息的公众号是绑定关联关系，暂不支持小游戏）
         */
        var appid: String? = null

        /**
         * 所需跳转到小程序的具体页面路径，支持带参数,（示例index?foo=bar），要求该小程序已发布，暂不支持小游戏
         */
        var pagepath: String? = null
    }

    class Item {
        var value: String? = null

        /**
         * 模板内容字体颜色，不填默认为黑色
         */
        var color: String? = null

        constructor()
        constructor(value: String) {
            this.value = value
        }

        constructor(value: String, color: String? = null) {
            this.value = value
            this.color = color
        }
    }

    /**
     * 模板数据
     */
    var data: MutableMap<String, Item>? = null

    /**
     * 跳小程序所需数据，不需跳小程序可不用传该数据
     */
    var miniprogram: MiniProgram? = null

    /**
     * 模板ID
     */
    @JSONField(name = "template_id")
    var templateId: String? = null

    /**
     * 接收者openid
     */
    var touser: String? = null

    /**
     * 模板跳转链接（海外帐号没有跳转能力）
     */
    var url: String? = null
}