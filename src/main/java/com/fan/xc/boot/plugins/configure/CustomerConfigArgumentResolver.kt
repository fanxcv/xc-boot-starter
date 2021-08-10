package com.fan.xc.boot.plugins.configure

import com.fan.xc.boot.starter.interfaces.XcHandlerMethodArgumentResolver
import com.fan.xc.boot.starter.utils.Conversion
import org.springframework.beans.TypeMismatchException
import org.springframework.beans.factory.BeanFactory
import org.springframework.core.MethodParameter
import org.springframework.util.StringUtils
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import org.springframework.web.method.support.ModelAndViewContainer

class CustomerConfigArgumentResolver(private val configs: ConfigureCache, private val beanFactory: BeanFactory) :
        XcHandlerMethodArgumentResolver {
    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.hasParameterAnnotation(InjectConfig::class.java)
    }

    override fun resolveArgument(parameter: MethodParameter, mavContainer: ModelAndViewContainer?,
                                 webRequest: NativeWebRequest, binderFactory: WebDataBinderFactory?): Any? {
        val nestedParameter = parameter.nestedIfOptional()
        val annotation: InjectConfig = nestedParameter.getParameterAnnotation(InjectConfig::class.java)
                ?: error("non InjectConfig annotation")

        val name: String = if (StringUtils.hasText(annotation.value)) annotation.value else nestedParameter.parameterName!!
        var v = CustomerConfigUtils.convertValue(beanFactory, configs[name], annotation)
        // 做参数转换
        if (v != null) {
            val clazz: Class<*> = nestedParameter.parameterType
            if (clazz.isAssignableFrom(v.javaClass)) {
                return v
            } else {
                v = try {
                    Conversion.binder.convertIfNecessary(v, clazz, nestedParameter)
                } catch (ex: TypeMismatchException) {
                    throw MethodArgumentTypeMismatchException(v, ex.requiredType, name, nestedParameter, ex.cause!!)
                }
            }
        }
        return v
    }
}