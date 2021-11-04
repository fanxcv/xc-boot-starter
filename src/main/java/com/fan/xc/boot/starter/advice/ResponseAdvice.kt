package com.fan.xc.boot.starter.advice

import com.fan.xc.boot.starter.Dict
import com.fan.xc.boot.starter.event.Event
import com.fan.xc.boot.starter.event.Return.success
import org.springframework.core.MethodParameter
import org.springframework.http.MediaType
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.http.server.ServletServerHttpRequest
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice

/**
 * @author fan
 */
@ControllerAdvice
class ResponseAdvice : ResponseBodyAdvice<Any> {
    override fun supports(returnType: MethodParameter, converterType: Class<out HttpMessageConverter<*>>): Boolean {
        return true
    }

    override fun beforeBodyWrite(body: Any?, returnType: MethodParameter, selectedContentType: MediaType, selectedConverterType: Class<out HttpMessageConverter<*>>, request: ServerHttpRequest, response: ServerHttpResponse): Any? {
        if (body == null) {
            return success()
        }

        if (body is Event) {
            return body
        }

        if (request is ServletServerHttpRequest) {
            val o = request.servletRequest.getAttribute(Dict.REQUEST_DEAL_BY_XC_CORE)
            if (o != null && o as Boolean) {
                return success(body)
            }
        }

        return body
    }
}
