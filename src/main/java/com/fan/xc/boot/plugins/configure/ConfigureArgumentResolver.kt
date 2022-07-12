package com.fan.xc.boot.plugins.configure

import com.fan.xc.boot.starter.Dict
import com.fan.xc.boot.starter.interfaces.XcHandlerMethodArgumentResolver
import com.fan.xc.boot.starter.utils.Conversion
import org.springframework.beans.TypeMismatchException
import org.springframework.core.MethodParameter
import org.springframework.core.env.Environment
import org.springframework.util.StringUtils
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import org.springframework.web.method.support.ModelAndViewContainer

class ConfigureArgumentResolver(private val environment: Environment) : XcHandlerMethodArgumentResolver {
    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.hasParameterAnnotation(InjectConfig::class.java)
    }

    override fun resolveArgument(parameter: MethodParameter, mavContainer: ModelAndViewContainer?,
                                 webRequest: NativeWebRequest, binderFactory: WebDataBinderFactory?): Any? {
        val nestedParameter = parameter.nestedIfOptional()
        val annotation: InjectConfig = nestedParameter.getParameterAnnotation(InjectConfig::class.java)
            ?: error("non InjectConfig annotation")

        val name: String = if (StringUtils.hasText(annotation.value)) annotation.value else nestedParameter.parameterName!!

        val v = environment.getProperty(name) ?: {
            val defaultValue = annotation.defaultValue
            if (defaultValue != Dict.DEFAULT_VALUE) {
                defaultValue
            } else {
                null
            }
        }

        return try {
            val clazz: Class<*> = nestedParameter.parameterType
            Conversion.binder.convertIfNecessary(v, clazz, nestedParameter)
        } catch (ex: TypeMismatchException) {
            throw MethodArgumentTypeMismatchException(v, ex.requiredType, name, nestedParameter, ex.cause!!)
        }
    }
}