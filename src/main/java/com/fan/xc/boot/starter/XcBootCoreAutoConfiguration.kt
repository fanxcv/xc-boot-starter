package com.fan.xc.boot.starter

import com.fan.xc.boot.starter.adapter.XcRequestMappingHandlerAdapter
import com.fan.xc.boot.starter.configuration.XcConfiguration
import com.fan.xc.boot.starter.converters.JsonArray2ListGenericConverter
import com.fan.xc.boot.starter.converters.JsonObject2EntityConverterFactory
import com.fan.xc.boot.starter.converters.String2EntityConverterFactory
import com.fan.xc.boot.starter.converters.String2ListGenericConverter
import com.fan.xc.boot.starter.interceptor.CoreInterceptor
import lombok.extern.slf4j.Slf4j
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.AutoConfigureAfter
import org.springframework.boot.autoconfigure.AutoConfigureOrder
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.format.FormatterRegistry
import org.springframework.web.servlet.DispatcherServlet
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter
import javax.servlet.Servlet

@Slf4j
@Configuration
@ComponentScan
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
@AutoConfigureAfter(WebMvcAutoConfiguration::class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConditionalOnClass(Servlet::class, DispatcherServlet::class, WebMvcConfigurer::class)
@ConditionalOnProperty(prefix = "xc.core", value = ["enable"], havingValue = "true", matchIfMissing = true)
open class XcBootCoreAutoConfiguration(xcConfig: XcConfiguration) : WebMvcRegistrations, WebMvcConfigurer, ApplicationContextAware {
    private val log: Logger = LoggerFactory.getLogger(XcBootCoreAutoConfiguration::class.java)
    private lateinit var applicationContext: ApplicationContext
    private val config: XcConfiguration.CoreConfig = xcConfig.core

    override fun getRequestMappingHandlerAdapter(): RequestMappingHandlerAdapter = XcRequestMappingHandlerAdapter()

    override fun addInterceptors(registry: InterceptorRegistry) {
        log.info("===> init xc-boot-core in {}, exclude {}", config.path, config.excludePath)
        registry.addInterceptor(CoreInterceptor(applicationContext))
                .excludePathPatterns(config.excludePath)
                .order(Ordered.HIGHEST_PRECEDENCE)
                .addPathPatterns(config.path)
    }

    override fun addFormatters(registry: FormatterRegistry) {
        // 注册JSONObject转Entity的转换器
        registry.addConverterFactory(JsonObject2EntityConverterFactory())
        registry.addConverter(JsonArray2ListGenericConverter())
        registry.addConverter(String2EntityConverterFactory())
        registry.addConverter(String2ListGenericConverter())
    }

    override fun setApplicationContext(applicationContext: ApplicationContext) {
        this.applicationContext = applicationContext
    }
}