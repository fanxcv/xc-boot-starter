package com.fan.xc.boot.starter.handler

import com.alibaba.fastjson.JSONObject
import com.fan.xc.boot.starter.annotation.InjectEntity
import com.fan.xc.boot.starter.annotation.VerifyParam
import com.fan.xc.boot.starter.event.EventImpl
import org.springframework.beans.factory.BeanFactory
import org.springframework.core.MethodParameter
import org.springframework.lang.NonNull
import org.springframework.util.StringUtils
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

/**
 * 给有ReturnJson注解的方法注入Event对象
 */
class InjectEntityHandlerMethodArgumentResolver(beanFactory: BeanFactory) : BaseVerify(beanFactory), HandlerMethodArgumentResolver {

    override fun supportsParameter(param: MethodParameter): Boolean {
        return param.hasParameterAnnotation(InjectEntity::class.java)
    }

    override fun resolveArgument(@NonNull parameter: MethodParameter,
                                 modelAndViewContainer: ModelAndViewContainer?,
                                 @NonNull nativeWebRequest: NativeWebRequest,
                                 webDataBinderFactory: WebDataBinderFactory?): Any? {
        val event = EventImpl.getEvent()
        val nestedParameter = parameter.nestedIfOptional()

        val injectEntity: InjectEntity = nestedParameter.getParameterAnnotation(InjectEntity::class.java)!!

        val value: String = injectEntity.value
        val key: String = if (StringUtils.hasText(value)) value else nestedParameter.parameterName!!
        val clazz: Class<*> = nestedParameter.parameterType

        val list: List<VerifyParam> = getVerifyList(key, parameter)
        val params: MutableMap<String, Any?> = event.getParamMap()
        list.forEach { checkOneParam(params, it, key) }

        // 默认将所有参数整合为一个bean注入
        return JSONObject(params).toJavaObject(clazz)
    }

    private fun checkOneParam(event: MutableMap<String, Any?>, verify: VerifyParam, key: String) {
        verify.name.forEach {
            if (!it.contains('.')) {
                event[it] = check(it, event[it], verify)
            } else if (it.startsWith("$key.")) {
                val k = it.substring(it.indexOf('.') + 1)
                event[k] = check(it, event[k], verify)
            }
        }
    }
}