package com.fan.xc.boot.starter.interceptor

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.parser.Feature
import com.fan.xc.boot.plugins.api.gateway.XcGatewayHandler
import com.fan.xc.boot.starter.Dict
import com.fan.xc.boot.starter.interfaces.XcEventInterface
import com.fan.xc.boot.starter.enums.ReturnCode
import com.fan.xc.boot.starter.event.EventImpl
import com.fan.xc.boot.starter.event.EventInner
import com.fan.xc.boot.starter.exception.XcRunException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.NoSuchBeanDefinitionException
import org.springframework.context.ApplicationContext
import org.springframework.core.ParameterizedTypeReference
import org.springframework.core.annotation.AnnotationAwareOrderComparator
import org.springframework.http.MediaType
import org.springframework.util.StringUtils
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import java.nio.charset.Charset
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * 核心拦截器, 主要处理请求参数
 */
class CoreInterceptor(applicationContext: ApplicationContext) : HandlerInterceptor {
    private val log: Logger = LoggerFactory.getLogger(CoreInterceptor::class.java)
    private val type = object : ParameterizedTypeReference<Map<String?, Any?>?>() {}.type
    private val gatewayHandler: Collection<XcGatewayHandler>?
    private val eventInterface: XcEventInterface?

    init {
        eventInterface = try {
            applicationContext.getBean(XcEventInterface::class.java)
        } catch (e: NoSuchBeanDefinitionException) {
            log.warn("no instance of XcEventInterface, don't init it")
            null
        }

        gatewayHandler = try {
            val collection = applicationContext.getBeansOfType(XcGatewayHandler::class.java).values.toMutableList()
            AnnotationAwareOrderComparator.sort(collection)
            collection
        } catch (e: Exception) {
            log.error("no instance of XcGatewayHandler", e)
            null
        }
    }

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        if (handler is HandlerMethod) {
            val event = EventImpl.instance()
            val ip = getIpAddress(request)
            event.setUrl(request.requestURI)
            event.setIp(ip)

            // 解析Token
            event.setToken(eventInterface?.parseToken(request))

            // 网关拦截器
            gatewayHandler?.forEach { if (!it.check(handler, request, event)) throw XcRunException(ReturnCode.CHECK_FAIL) }

            // 标记该请求有使用XcCore处理
            request.setAttribute(Dict.REQUEST_DEAL_BY_XC_CORE, true)
            getRequestParam(request, event)
        }
        return true
    }

    override fun afterCompletion(request: HttpServletRequest, response: HttpServletResponse, handler: Any, ex: java.lang.Exception?) {
        val event = EventImpl.getEvent()
        eventInterface?.afterCompletion(event)
        // 请求完成后需要初始化event对象
        event.init()
    }

    private fun getRequestParam(request: HttpServletRequest, input: EventInner) {
        //先直接获取参数
        request.parameterMap?.forEach { (k: String, v: Array<String>?) -> input.putParam(k, if (v.size == 1) v[0] else listOf(*v)) }
        // 处理json和xml两种数据
        val contentType = request.contentType
        if (StringUtils.hasText(contentType)) {
            //尝试读取请求消息体，如果获取到了消息体，尝试读取里面的参数
            try {
                dealRequestBody(input, request, contentType)
            } catch (e: Exception) {
                log.error("params parse error", e.message)
            }
        }
    }

    private fun dealRequestBody(input: EventInner, request: HttpServletRequest, contentType: String) {
        if (contentType.contains(MediaType.APPLICATION_JSON_VALUE)) { //处理Json
            val map: Map<String, Any?> = JSON.parseObject(request.inputStream,
                    Charset.forName(request.characterEncoding),
                    type, Feature.UseBigDecimal)
            input.putParamAll(map)
        } else if (contentType.contains(MediaType.APPLICATION_XML_VALUE)) { //处理xml数据
            log.warn("not support xml parse")
            // Map<String, String> map = XmlTools.xml2Map(line);
            // input.putParamAll(map);
        }
    }

    private fun getIpAddress(request: HttpServletRequest): String? {
        var ip = request.getHeader(Dict.X_FORWARDED_FOR)
        if (checkIP(ip)) ip = request.getHeader(Dict.PROXY_CLIENT_IP)
        if (checkIP(ip)) ip = request.getHeader(Dict.WL_PROXY_CLIENT_IP)
        if (checkIP(ip)) ip = request.getHeader(Dict.HTTP_CLIENT_IP)
        if (checkIP(ip)) ip = request.getHeader(Dict.HTTP_X_FORWARDED_FOR)
        if (checkIP(ip)) ip = request.remoteAddr
        return ip
    }

    private fun checkIP(ip: String?): Boolean = ip == null || ip.isEmpty() || Dict.UNKNOWN.equals(ip, ignoreCase = true)
}