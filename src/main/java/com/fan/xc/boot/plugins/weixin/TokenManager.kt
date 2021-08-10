package com.fan.xc.boot.plugins.weixin

interface TokenManager {
    /**
     * 获取Token
     */
    fun token(): String

    /**
     * 获取Token到期时间
     */
    fun expires(): Long

    /**
     * 手动刷新Token
     */
    //fun refresh(): String

    /**
     * 初始化锁
     */
    fun initLock()
}