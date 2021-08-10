package com.fan.xc.boot.starter.adapter

import com.fan.xc.boot.starter.exception.XcRunException
import com.fan.xc.boot.starter.handler.EventHandlerMethodArgumentResolver
import com.fan.xc.boot.starter.handler.InjectEntityHandlerMethodArgumentResolver
import com.fan.xc.boot.starter.handler.VerifyParamHandlerMethodArgumentResolver
import com.fan.xc.boot.starter.interfaces.XcHandlerMethodArgumentResolver
import org.springframework.lang.NonNull
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter
import java.util.*
import java.util.stream.Collectors

/**
 * 重写请求适配器，处理相关请求
 * @author fan
 */
class XcRequestMappingHandlerAdapter : RequestMappingHandlerAdapter() {
    override fun afterPropertiesSet() {
        super.afterPropertiesSet()

        // 添加自定义参数解析器
        val argumentResolvers = argumentResolvers
        val xcArgumentResolvers = customXcArgumentResolvers
        val initBinderArgumentResolvers = initBinderArgumentResolvers

        if (argumentResolvers == null) {
            throw XcRunException("argumentResolvers is null")
        } else {
            val resolvers = addCustomerArgumentResolvers(argumentResolvers, xcArgumentResolvers)
            setArgumentResolvers(resolvers)
        }

        if (initBinderArgumentResolvers == null) {
            throw XcRunException("initBinderArgumentResolvers is null")
        } else {
            val resolvers = addCustomerArgumentResolvers(initBinderArgumentResolvers, xcArgumentResolvers)
            setInitBinderArgumentResolvers(resolvers)
        }
    }

    private fun addCustomerArgumentResolvers(baseArgumentResolvers: List<HandlerMethodArgumentResolver>?,
                                             xcArgumentResolvers: List<HandlerMethodArgumentResolver>): List<HandlerMethodArgumentResolver> {
        val resolvers: MutableList<HandlerMethodArgumentResolver> = ArrayList()

        // 添加自定义的参数解析器
        resolvers.add(EventHandlerMethodArgumentResolver())
        resolvers.add(VerifyParamHandlerMethodArgumentResolver(beanFactory ?: error("beanFactory not be null")))
        resolvers.add(InjectEntityHandlerMethodArgumentResolver(beanFactory ?: error("beanFactory not be null")))
        if (xcArgumentResolvers.isNotEmpty()) {
            resolvers.addAll(xcArgumentResolvers)
        }
        if (baseArgumentResolvers != null) {
            resolvers.addAll(baseArgumentResolvers)
        }
        return resolvers
    }

    /**
     * 获取自定义的XcHandlerMethodArgumentResolver列表
     */
    @get:NonNull
    private val customXcArgumentResolvers: List<HandlerMethodArgumentResolver>
        get() {
            val resolvers = customArgumentResolvers
            return if (resolvers != null) {
                // 从自定义参数转换器中获取实现XcHandlerMethodArgumentResolver接口的转换器
                resolvers.stream().filter { it is XcHandlerMethodArgumentResolver }
                        .collect(Collectors.toList())
            } else {
                ArrayList()
            }
        }
}