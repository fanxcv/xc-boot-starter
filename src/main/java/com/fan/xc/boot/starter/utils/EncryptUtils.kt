package com.fan.xc.boot.starter.utils

import com.fan.xc.boot.starter.Dict
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.nio.charset.StandardCharsets
import java.security.MessageDigest

/**
 * 加密实现,支持sha1和MD5
 * @author fan
 */
object EncryptUtils {
    private val log: Logger = LoggerFactory.getLogger(EncryptUtils::class.java)
    private val hexDigits = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f')

    @JvmStatic
    fun sha1(str: String?): String = generate(str, "SHA1")

    @JvmStatic
    fun md5(str: String?): String = generate(str, "MD5")

    private fun generate(s: String?, algorithm: String): String {
        if (s == null || s.isEmpty()) {
            return Dict.BLANK
        }
        return try {
            // 获得MD5摘要算法的 MessageDigest 对象
            val mdTemp = MessageDigest.getInstance(algorithm)
            // 使用指定的字节更新摘要
            mdTemp.update(s.toByteArray(StandardCharsets.UTF_8))
            // 获得密文
            val md = mdTemp.digest()
            // 把密文转换成十六进制的字符串形式
            val str = CharArray(md.size * 2)
            var k = 0
            for (byte in md) {
                val i = byte.toInt()
                str[k++] = hexDigits[i ushr 4 and 0xf]
                str[k++] = hexDigits[i and 0xf]
            }
            String(str)
        } catch (e: Exception) {
            log.error(e.message, e)
            Dict.BLANK
        }
    }
}