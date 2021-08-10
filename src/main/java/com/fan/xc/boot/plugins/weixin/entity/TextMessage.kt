package com.fan.xc.boot.plugins.weixin.entity

/**
 * @author fan
 */
class TextMessage : CustomMessage() {
    class Text {
        constructor()
        constructor(content: String?) {
            this.content = content
        }

        var content: String? = null
    }

    /**
     * 文本消息内容
     */
    var text: Text? = null
}