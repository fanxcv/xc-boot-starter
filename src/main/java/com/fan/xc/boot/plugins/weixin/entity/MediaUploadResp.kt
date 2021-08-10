package com.fan.xc.boot.plugins.weixin.entity

import com.alibaba.fastjson.annotation.JSONField

class MediaUploadResp : WXBaseResp() {
    var type: String? = null
    @JSONField(name = "media_id")
    var mediaId: String? = null
    @JSONField(name = "created_at")
    var createdAt: Long? = null
}