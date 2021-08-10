package com.fan.xc.boot.starter.handler.chain

import com.fan.xc.boot.starter.annotation.VerifyParam
import com.fan.xc.boot.starter.exception.ParamErrorException
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import java.util.concurrent.ConcurrentHashMap

/**
 * 执行正则校验
 */
@Component
class RegexCheckChain : AbstractVerifyChain() {
    companion object {
        // 用于缓存正则,避免每次都实例化正则
        private val cache: ConcurrentHashMap<String, Regex> = ConcurrentHashMap()
    }

    override fun doExec(key: String, value: Any?, verify: VerifyParam): Pair<Boolean, Any?> {
        val reg: String = verify.regex
        // 如果没有书写正则, 直接放过
        if (!StringUtils.hasText(reg)) {
            return Pair(true, value)
        }
        val regex: Regex = cache[reg] ?: {
            val r = reg.toRegex(RegexOption.IGNORE_CASE)
            cache[reg] = r
            r
        }()
        if (value == null || !regex.matches(value.toString())) {
            throw ParamErrorException("Param [$key: '$value'] check fail; Regex: $reg")
        }
        return Pair(true, value)
    }
}