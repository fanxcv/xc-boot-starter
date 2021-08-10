package com.fan.xc.boot.starter.utils

import org.slf4j.Logger
import org.slf4j.LoggerFactory

object Tools {
    private val log: Logger = LoggerFactory.getLogger(Tools::class.java)
    private val innerList: List<Pair<Int, Int>> by lazy {
        // 内网地址包括 10/8, 192.168/16, 169.254/16, 127/8, 172.16/12
        // 172\.1[6-9]{1}\.\d{1,3}\.\d{1,3}|172\.2[0-9]{1}\.\d{1,3}\.\d{1,3}| 172\.3[0-1]{1}\.\d{1,3}\.\d{1,3}
        listOf(Pair(ip2Int("10.0.0.0"), ip2Int("10.255.255.255")),
                Pair(ip2Int("192.168.0.0"), ip2Int("192.168.255.255")),
                Pair(ip2Int("127.0.0.0"), ip2Int("127.255.255.255")),
                Pair(ip2Int("172.16.0.0.0"), ip2Int("172.31.255.255")),
                Pair(ip2Int("169.254.0.0"), ip2Int("169.254.255.255")))
    }

    /**
     * 计算大于等于输入数字，且为2的n次方的数
     */
    @JvmStatic
    fun nextPowerOf2(num: Int): Int {
        var n = num - 1
        n = n or n.ushr(1)
        n = n or n.ushr(2)
        n = n or n.ushr(4)
        n = n or n.ushr(8)
        n = n or n.ushr(16)
        return n + 1
    }

    /**
     * 判断IP是否在设置的IP域里面
     * @param ip     待判断的IP
     * @param ipArea 待检查的域
     * @return 是否满足
     */
    @JvmStatic
    fun ipIsInCidr(ip: String?, ipArea: List<String>?): Boolean {
        if (ip == null || ip.isNullOrBlank() || ipArea == null || ipArea.isEmpty()) {
            return false
        }

        val ipAddress = ip2Int(ip)

        for (s in ipArea) {
            if (!s.contains("/")) {
                // 如果IP是完整IP,直接比较
                if (s == ip) {
                    return true
                }
                continue
            }
            //分隔IP
            val split = s.split("/")
            //掩码（0-32）
            val type = split[1].toInt()
            //匹配的位数为32 - type位（16进制的1）
            val mask = if (type == 0) 0 else -0x1 shl 32 - type
            val cidrIpAddr = ip2Int(split[0])
            if (ipAddress and mask == cidrIpAddr and mask) {
                return true
            }
        }
        return false
    }

    @JvmStatic
    fun ip2Int(ip: String?): Int {
        if (ip == null || ip.isNullOrBlank()) {
            return 0
        }

        val split = ip.split('.')
        if (split.size != 4) {
            log.error("IP转Int失败, 失败IP为: $ip")
            return 0
        }

        return split[0].toInt() shl 24 or
                (split[1].toInt() shl 16) or
                (split[2].toInt() shl 8) or
                split[3].toInt()
    }

    @JvmStatic
    fun isInnerIp(ip: String?): Boolean {
        if (ip == null || ip.isNullOrBlank()) {
            return false
        }

        val int = ip2Int(ip)

        for (pair in innerList) {
            if (pair.first <= int && pair.second >= int) {
                return true
            }
        }

        return false
    }
}