package com.fan.xc.boot.starter.enums

/**
 * @author fan
 * @date Create in 10:53 2019-05-08
 */
enum class ReturnCode(
        /**
         * 错误码
         */
        private val code: Int,
        /**
         * 错误信息
         */
        private val message: String) {
    SUCCESS(0, "success"),// 成功
    FAIL(-1, "error"),// 失败 默认
    PARAM_ERROR(-2, "Parameter error"),// 参数错误
    AUTH_FAIL(-8, "Permission denied"),// 用户权限不足
    NOT_LOGIN(-9, "User not login"),// 用户未登录
    CHECK_FAIL(-7, "Failed to check permissions"),// 无权访问该请求
    SYSTEM_ERROR(999999, "System error");// 系统错误

    fun message() = message

    fun code() = code
}