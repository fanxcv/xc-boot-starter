package com.fan.xc.boot.plugins.configure

import com.fan.xc.boot.starter.Dict
import com.fan.xc.boot.starter.interfaces.ParamMap
import org.springframework.beans.factory.BeanFactory
import kotlin.reflect.KClass

object CustomerConfigUtils {
    fun convertValue(beanFactory: BeanFactory, value: Any?, annotation: InjectConfig): Any? {
        var v = value
        if (v == null || "" == v) {
            // 处理默认值
            val defaultValue = annotation.defaultValue
            if (defaultValue != Dict.DEFAULT_VALUE) {
                v = defaultValue
            }
        } else {
            //参数预处理
            val maps: Array<KClass<out ParamMap>> = annotation.map
            for (map in maps) {
                val clazz: ParamMap = beanFactory.getBean(map.java)
                v = clazz.map(v)
            }
        }
        return v
    }
}