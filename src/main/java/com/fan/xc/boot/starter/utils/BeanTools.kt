package com.fan.xc.boot.starter.utils

import com.google.common.base.CaseFormat
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.lang.reflect.Field
import java.util.*
import java.util.function.Function

/**
 * @author yangfan323
 */
object BeanTools {
    private val log: Logger = LoggerFactory.getLogger(BeanTools::class.java)

    /**
     * Bean的转换，调用set方法
     * 已需要转换的bean字段为基础, 从源对象提取数据
     * @param source 需要转换的Bean
     * @param clazz  需要转换成的Bean的Class
     * @return 转换成功的对象
     */
    @JvmStatic
    fun <S : Any, D> beanToBean(source: S?, clazz: Class<D>?): D? {
        return beanToBean(source, clazz, null)
    }

    /**
     * Bean的转换，支持map转bean,不支持bean转map
     * 已需要转换的bean字段为基础, 从源对象提取数据
     * 通过mapValues预设的转换规则, 可以做数据转换或字段映射
     * @param source    需要转换的Bean
     * @param clazz     需要转换成的Bean的Class
     * @param mapValues 用于转换字段值,Function入参为原始对象,出参为新字段值
     * @return 转换成功的对象
     */
    @JvmStatic
    fun <S : Any, D> beanToBean(source: S?, clazz: Class<D>?, mapValues: Map<String, Function<S, Any?>>?): D? {
        if (source == null || clazz == null) {
            return null
        }

        val oClazz: Class<*> = source.javaClass
        val oFields = oClazz.declaredFields
        val fields = clazz.declaredFields
        return try {
            val t = clazz.newInstance()
            //仅当两个都有属性的时候才遍历
            if (fields.isEmpty() || oFields.isEmpty()) {
                return t
            }
            for (field in fields) {
                // 获取字段名
                val name = field.name
                // 获取值,先尝试从映射表中获取,获取不到的话尝试从原始数据类获取
                var res = mapValues?.get(name)?.apply(source) ?: tryGetSourceValue(source, oFields, name)
                // 尝试转换下参数对象
                res = Conversion.binder.convertIfNecessary(res, field.type)
                // 设置值
                field.isAccessible = true
                field.set(t, res)
            }
            t
        } catch (e: Exception) {
            log.error(e.message, e)
            throw RuntimeException("copy bean fail")
        }
    }

    private fun <S : Any> tryGetSourceValue(source: S, oFields: Array<Field>, name: String): Any? {
        return try {
            // 如果source是map
            if (Map::class.java.isAssignableFrom(source.javaClass)) {
                // 先尝试用原始字段名获取属性, 获取不到的话尝试将驼峰转为下划线后再尝试获取
                (source as Map<*, *>)[name] ?: source[CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, name)]
            } else {
                val field = oFields.first { name == it.name }
                field.isAccessible = true
                field.get(source)
            }
        } catch (e: NoSuchElementException) {
            null
        }
    }
}