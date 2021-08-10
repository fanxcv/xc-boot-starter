package com.fan.xc.boot.starter.handler.chain

import com.fan.xc.boot.starter.annotation.VerifyParam
import org.springframework.beans.factory.BeanFactory

/**
 * 使用带参数的ParamCheck校验
 */
class CheckAnnotationChain(private val beanFactory: BeanFactory) : AbstractVerifyChain() {
    override fun doExec(key: String, value: Any?, verify: VerifyParam): Pair<Boolean, Any?> {
        // TODO 待实现
        return Pair(true, value)
    }
}