package com.fan.xc.boot.starter.handler.chain

import com.fan.xc.boot.starter.annotation.VerifyParam
import com.fan.xc.boot.starter.interfaces.ParamMap
import org.springframework.beans.factory.BeanFactory
import org.springframework.stereotype.Component
import kotlin.reflect.KClass

/**
 * 参数预处理
 */
@Component
class ParamMapChain(private val beanFactory: BeanFactory) : AbstractVerifyChain() {
    companion object {
        private val beanCache: MutableMap<KClass<out ParamMap>, ParamMap> = HashMap()
    }

    override fun doExec(key: String, value: Any?, verify: VerifyParam): Pair<Boolean, Any?> {
        var v = value
        //参数预处理
        val maps: Array<KClass<out ParamMap>> = verify.map
        for (map in maps) {
            val clazz: ParamMap = beanCache[map] ?: {
                val bean = beanFactory.getBean(map.java)
                beanCache[map] = bean
                bean
            }()
            v = clazz.map(v)
        }
        return Pair(true, v)
    }
}