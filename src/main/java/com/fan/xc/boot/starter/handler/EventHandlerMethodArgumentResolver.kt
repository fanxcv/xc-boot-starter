package com.fan.xc.boot.starter.handler

import com.fan.xc.boot.starter.event.Event
import com.fan.xc.boot.starter.event.EventImpl
import org.springframework.core.MethodParameter
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

/**
 * 给有ReturnJson注解的方法注入Event对象
 */
class EventHandlerMethodArgumentResolver : HandlerMethodArgumentResolver {
    override fun supportsParameter(param: MethodParameter): Boolean {
        return Event::class.java == param.parameterType
    }

    override fun resolveArgument(parameter: MethodParameter,
                                 mavContainer: ModelAndViewContainer?,
                                 webRequest: NativeWebRequest,
                                 binderFactory: WebDataBinderFactory?): Any {
        return EventImpl.getEvent()
    }
}
