package com.fan.xc.boot.starter.check

import com.fan.xc.boot.starter.interfaces.ParamCheck
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component
import org.springframework.util.ObjectUtils

@Lazy
@Component
open class NotEmpty : ParamCheck {
    override fun check(value: Any?, vararg args: String?): Boolean {
        return when (value) {
            null -> false
            is String -> value.isNotEmpty()
            is Array<*> -> value.isNotEmpty()
            is Map<*, *> -> value.isNotEmpty()
            is Collection<*> -> value.isNotEmpty()
            else -> !ObjectUtils.isEmpty(value)
        }
    }
}