package com.fan.xc.boot.starter.handler.chain

import com.fan.xc.boot.starter.annotation.VerifyParam
import com.fan.xc.boot.starter.exception.ParamErrorException
import com.fan.xc.boot.starter.interfaces.ParamCheck
import org.springframework.beans.factory.BeanFactory
import org.springframework.stereotype.Component
import kotlin.reflect.KClass

/**
 * 使用ParamCheck实现类校验
 */
@Component
class ParamCheckChain(private val beanFactory: BeanFactory) : AbstractVerifyChain() {
    companion object {
        private val beanCache: MutableMap<KClass<out ParamCheck>, ParamCheck> = HashMap()
    }

    override fun doExec(key: String, value: Any?, verify: VerifyParam): Pair<Boolean, Any?> {
        val checks: Array<KClass<out ParamCheck>> = verify.value
        var v = value

        for (check in checks) {
            // 先从缓存获取, 如果获取失败了再从beanFactory中获取
            val clazz: ParamCheck = beanCache[check] ?: {
                val a = beanFactory.getBean(check.java)
                beanCache[check] = a
                a
            }()
            // 校验时都使用原始值
            if (!clazz.check(value)) {
                log.error("Param [$key: '$value'] check fail; Verify: " + check.simpleName)
                throw ParamErrorException(clazz.message(key, value))
            } else {
                // 这里可以使用check里面的map转换数据,但是不建议这么做
                v = clazz.map(v)
            }
        }

        return Pair(true, v)
    }
}