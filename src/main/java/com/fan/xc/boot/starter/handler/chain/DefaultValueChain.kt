package com.fan.xc.boot.starter.handler.chain

import com.fan.xc.boot.starter.Dict
import com.fan.xc.boot.starter.annotation.VerifyParam
import com.fan.xc.boot.starter.exception.ParamErrorException
import org.springframework.stereotype.Component

/**
 * 判断是否需要使用默认值
 * 如果值是必须的,但是输入为null且defaultValue存在,会使用默认值
 */
@Component
class DefaultValueChain : AbstractVerifyChain() {
    override fun doExec(key: String, value: Any?, verify: VerifyParam): Pair<Boolean, Any?> {
        // 获取默认值
        val defaultValue: String? = if (Dict.DEFAULT_VALUE == verify.defaultValue) null else verify.defaultValue
        // 获取是否必须输入
        val require: Boolean = verify.required
        // 如果值是必须的,并且value为null,有默认值就返回默认值,没有就返回异常
        if (require && value == null) {
            return Pair(false, defaultValue ?: throw ParamErrorException("Param [$key] is required"))
        }
        // 如果值是非必须的,并且value和defaultValue都为null,那就直接注入null了,不在执行后续流程
        if (!require && value == null && defaultValue == null) {
            return Pair(false, null)
        }
        // 优先返回value, 如果value为null ,则返回defaultValue
        return Pair(true, value ?: defaultValue)
    }
}
