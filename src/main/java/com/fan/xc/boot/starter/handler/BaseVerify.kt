package com.fan.xc.boot.starter.handler

import com.fan.xc.boot.starter.handler.chain.*
import com.google.common.collect.Lists
import com.fan.xc.boot.starter.annotation.VerifyParam
import com.fan.xc.boot.starter.annotation.VerifyParams
import org.springframework.beans.factory.BeanFactory
import org.springframework.core.MethodParameter
import java.util.concurrent.ConcurrentHashMap

/**
 * @author fan
 * @date Create in 14:53 2019-05-06
 */
open class BaseVerify(beanFactory: BeanFactory) {
    companion object {
        // 全局缓存, 避免每次都需要初始化
        private val cache: ConcurrentHashMap<MethodParameter, MutableList<VerifyParam>> = ConcurrentHashMap(64)
    }

    private val chain = AbstractVerifyChain.builder()
            .addChain(beanFactory.getBean(DefaultValueChain::class.java))
            .addChain(beanFactory.getBean(ParamCheckChain::class.java))
            .addChain(beanFactory.getBean(RegexCheckChain::class.java))
            .addChain(beanFactory.getBean(ParamMapChain::class.java))
            .build()

    /**
     * 参数校验
     * @param key    参数名
     * @param value  参数值
     * @param verify 校验的注解
     * @return java.lang.Object
     * @date 2019-05-06 14:55
     * @author fan
     */
    protected fun check(key: String?, value: Any?, verify: VerifyParam): Any? {
        if (key == null) return null
        return chain.exec(key, value, verify)
    }

    /**
     * 获取所有的参数校验
     * 先获取参数相关上的注解
     * 再获取方法上的注解, 再获取方法上的注解
     * 整合校验列表, 参数转换列表
     */
    protected fun getVerifyList(key: String, parameter: MethodParameter): List<VerifyParam> {
        var list: MutableList<VerifyParam>? = cache[parameter]

        if (list != null) {
            return list
        } else {
            list = Lists.newLinkedList()
        }

        //先获取参数注解
        val pv: VerifyParam? = parameter.getParameterAnnotation(VerifyParam::class.java)

        if (pv != null) {
            // 如果参数注解存在,就只以参数注解为准
            list.add(pv)
        } else {
            //再获取方法注解
            val vs: VerifyParams? = parameter.getMethodAnnotation(VerifyParams::class.java)
            val v: VerifyParam? = parameter.getMethodAnnotation(VerifyParam::class.java)
            if (v != null) add(key, v, list)
            vs?.value?.forEach { add(key, it, list) }
        }

        cache[parameter] = list
        return list
    }

    private fun add(key: String, v: VerifyParam, list: MutableList<VerifyParam>): Any? {
        //如果名字为空，对所有参数生效
        if (v.name.isEmpty()) list.add(v)
        else for (n in v.name) {
            when {
                key == n -> return list.add(v)
                n.startsWith("$key.") -> return list.add(v)
            }
        }
        return null
    }
}