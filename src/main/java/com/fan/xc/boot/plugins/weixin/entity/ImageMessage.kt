package com.fan.xc.boot.plugins.weixin.entity

import com.alibaba.fastjson.annotation.JSONField

/**
 * @author fan
 */
class ImageMessage : CustomMessage() {
    class Image {
        constructor()
        constructor(content: String?) {
            this.mediaId = content
        }

        @JSONField(name = "media_id")
        var mediaId: String? = null
    }

    /**
     * 图片消息内容
     */
    var image: Image? = null
}