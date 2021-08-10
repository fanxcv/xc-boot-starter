package com.fan.xc.boot.starter.check

import com.fan.xc.boot.starter.exception.ParamErrorException
import com.fan.xc.boot.starter.interfaces.ParamCheck
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils

@Lazy
@Component
open class HasText : ParamCheck {
    override fun check(value: Any?, vararg args: String?): Boolean {
        if (value !is String) {
            throw ParamErrorException("HasText only support check String")
        }
        return StringUtils.hasText(value)
    }
}