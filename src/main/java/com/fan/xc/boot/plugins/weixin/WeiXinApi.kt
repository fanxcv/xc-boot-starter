package com.fan.xc.boot.plugins.weixin

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.alibaba.fastjson.parser.Feature
import com.fan.xc.boot.plugins.weixin.entity.*
import com.fan.xc.boot.starter.Dict
import com.fan.xc.boot.starter.exception.XcRunException
import com.fan.xc.boot.starter.utils.EncryptUtils
import com.fan.xc.boot.starter.utils.NetUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.nio.charset.StandardCharsets

@Lazy
@Component
class WeiXinApi(private val jsApiTicketManager: JsApiTicketManager,
                private val accessTokenManager: AccessTokenManager,
                private val config: WeiXinConfig) {
    private val log: Logger = LoggerFactory.getLogger(WeiXinApi::class.java)

    /**
     * 微信服务器配置校验方法
     * 开发者通过检验signature对请求进行校验（下面有校验方式）。若确认此次GET请求来自微信服务器，请原样返回echostr参数内容，则接入生效，成为开发者成功，否则接入失败。加密/校验流程如下：
     * 1）将token、timestamp、nonce三个参数进行字典序排序
     * 2）将三个参数字符串拼接成一个字符串进行sha1加密
     * 3）开发者获得加密后的字符串可与signature对比，标识该请求来源于微信
     */
    fun checkSignature(nonce: String, timestamp: String, signature: String, echostr: String): String {
        val strings = arrayOf(nonce, timestamp, config.token)
        strings.sort()
        val signStr = "${strings[0]}${strings[1]}${strings[2]}"
        val sign = EncryptUtils.sha1(signStr)
        return if (sign == signature) echostr else Dict.BLANK
    }

    /**
     * JSTicket签名方法
     */
    fun jsTicketSign(noncestr: String, timestamp: Long, url: String): String {
        val ticket = jsApiTicketManager.token()
        val signStr = "jsapi_ticket=${ticket}&noncestr=${noncestr}&timestamp=${timestamp}&url=${url}"
        val signature = EncryptUtils.sha1(signStr)
        return signature ?: throw XcRunException("WeiXin jsSdk sign error")
    }

    /**
     * 获取微信AccessToken
     */
    fun getAccessToken(): String = accessTokenManager.token()

    /**
     * 获取微信AccessToken
     */
    fun getJsTicket(): String = jsApiTicketManager.token()

    /**
     * 获取微信AppId
     */
    fun getAppId(): String = config.appId

    /**
     * 微信获取AccessToken && 基础登录
     * {
     * "access_token": "ACCESS_TOKEN",
     * "expires_in": 7200,
     * "refresh_token": "REFRESH_TOKEN",
     * "openid": "OPENID",
     * "scope": "SCOPE",
     * }
     */
    fun webLoginBase(code: String): WXLoginResp {
        //2,通过code换取access_token
        return NetUtils.get(
                "https://api.weixin.qq.com/sns/oauth2/access_token?appid=${config.appId}&secret=${config.appSecret}&code=${code}&grant_type=authorization_code"
        ) { _, connection ->
            connection.inputStream.use {
                JSONObject.parseObject(it, WXLoginResp::class.java, Feature.UseBigDecimal)
            }
        }
    }

    /**
     * 微信获取用户个人信息
     * {
     * "openid": "OPENID",
     * "nickname": "NICKNAME",
     * "sex": 1,
     * "province": "PROVINCE",
     * "city": "CITY",
     * "country": "COUNTRY",
     * "headimgurl": "https://thirdwx.qlogo.cn/mmopen/g3MonUZtNHkdmzicIlibx6iaFqAc56vxLSUfpb6n5WKSYVY0ChQKkiaJSgQ1dZuTOgvLLrhJbERQQ4eMsv84eavHiaiceqxibJxCfHe/0",
     * "privilege": ["PRIVILEGE1", "PRIVILEGE2"],
     * "unionid": " o6_bmasdasdsad6_2sgVt7hMZOPfL"
     * }
     */
    fun webLoginUserInfo(code: String): WXUserInfo {
        val resp = webLoginBase(code)

        if (resp.accessToken == null) {
            val wxUserInfo = WXUserInfo()
            wxUserInfo.errcode = resp.errcode ?: -111111
            wxUserInfo.errmsg = resp.errmsg ?: "获取access token失败"
            return wxUserInfo
        }

        return NetUtils.get(
                "https://api.weixin.qq.com/sns/userinfo?access_token=${resp.accessToken}&openid=${resp.openid}&lang=zh_CN"
        ) { _, connection ->
            connection.inputStream.use {
                JSONObject.parseObject(it, WXUserInfo::class.java, Feature.UseBigDecimal)
            }
        }
    }

    /**
     * 客服接口-发消息
     * 文本
     * 图片
     */
    fun messageCustomSend(message: CustomMessage): Boolean {
        return NetUtils.post("https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=${accessTokenManager.token()}", JSON.toJSONString(message))
        { _, connection ->
            connection.inputStream.use {
                val bytes = it.readBytes()
                val resp: WXBaseResp = JSONObject.parseObject(bytes, WXBaseResp::class.java, Feature.UseBigDecimal)
                if (resp.errcode != null && resp.errcode != 0) {
                    log.debug("custom send result: {}", String(bytes, StandardCharsets.UTF_8))
                    throw XcRunException(resp.errcode ?: -999999, "${resp.errcode}: ${resp.errmsg}")
                } else {
                    return@post true
                }
            }
        }
    }

    /**
     * 发送模板消息
     */
    fun messageTemplateSend(message: TemplateMessage): Boolean {
        return NetUtils.post("https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=${accessTokenManager.token()}", JSON.toJSONString(message))
        { _, connection ->
            connection.inputStream.use {
                val bytes = it.readBytes()
                val resp: TemplateResp = JSONObject.parseObject(bytes, TemplateResp::class.java, Feature.UseBigDecimal)
                if (resp.errcode != null && resp.errcode != 0) {
                    log.debug("template send result: {}", String(bytes, StandardCharsets.UTF_8))
                    throw XcRunException(resp.errcode ?: -999999, "${resp.errcode}: ${resp.errmsg}")
                } else {
                    return@post true
                }
            }
        }
    }

    /**
     * 微信临时素材上传
     */
    fun mediaUpload(file: File, type: WxType): MediaUploadResp {
        return FileInputStream(file).use {
            mediaUpload(it, file.name, type)
        }
    }

    /**
     * 微信临时素材上传
     */
    fun mediaUpload(`is`: InputStream, filename: String, type: WxType): MediaUploadResp {
        return NetUtils.upload("https://api.weixin.qq.com/cgi-bin/media/upload?access_token=${accessTokenManager.token()}&type=$type", filename, `is`)
        { _, connection ->
            connection.inputStream.use {
                val bytes = it.readBytes()
                val resp: MediaUploadResp = JSONObject.parseObject(bytes, MediaUploadResp::class.java, Feature.UseBigDecimal)
                if (resp.errcode != null && resp.errcode != 0) {
                    log.debug("media upload result: {}", String(bytes, StandardCharsets.UTF_8))
                    throw XcRunException(resp.errcode ?: -999999, "${resp.errcode}: ${resp.errmsg}")
                } else {
                    return@upload resp
                }
            }
        }
    }
}