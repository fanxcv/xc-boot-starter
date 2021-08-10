package com.fan.xc.boot.starter.check

import com.fan.xc.boot.starter.interfaces.ParamCheck
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component

@Lazy
@Component
open class IsNumber : ParamCheck {
    override fun check(value: Any?, vararg args: String?): Boolean {
        return when (value) {
            is Number -> true
            is Char -> Character.isDigit(value)
            is String -> strIsNumber(value)
            else -> false
        }
    }

    private fun strIsNumber(value: String): Boolean {
        val length: Int = value.length
        for (i in 0 until length) {
            //判断是否为数字，不是返回false
            if (!Character.isDigit(value[i])) {
                return false
            }
        }
        return true
    }
}