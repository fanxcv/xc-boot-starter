package com.fan.xc.boot.plugins.configure

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.InitializingBean
import org.springframework.boot.env.RandomValuePropertySource
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.context.EnvironmentAware
import org.springframework.core.annotation.AnnotationAwareOrderComparator
import org.springframework.core.env.Environment
import org.springframework.core.env.MapPropertySource
import org.springframework.core.env.StandardEnvironment
import org.springframework.util.CollectionUtils
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

open class ConfigureConfiguration(private var eventSender: ConfigureEventSender?) : WebMvcConfigurer, InitializingBean, EnvironmentAware, ApplicationContextAware {
    private val log: Logger = LoggerFactory.getLogger(this::class.java)
    private lateinit var applicationContext: ApplicationContext
    private lateinit var environment: Environment

    /**
     * 注册CustomerConfigArgumentResolver，用于支持在MVC Controller中注入自定义参数
     */
    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        // 此处直接吧ConfigureCache实例注入，方便后面获取参数值
        resolvers.add(ConfigureArgumentResolver(environment))
    }

    override fun afterPropertiesSet() {
        val beans = applicationContext.getBeansOfType(IConfigureLoader::class.java)
        val loadConfigs: List<IConfigureLoader> = ArrayList<IConfigureLoader>(beans.values)
        if (CollectionUtils.isEmpty(loadConfigs)) {
            log.warn("===> none custom configure")
            return
        }

        // 获取Environment对象
        val environment = applicationContext.environment as StandardEnvironment
        val sources = environment.propertySources

        // 实现Spring的排序
        AnnotationAwareOrderComparator.sort(loadConfigs)
        loadConfigs.forEach {
            val map: Map<String, Any> = HashMap(16)
            it.load(map)
            // 将对应的资源放在RandomValuePropertySource前面,保证加载的远程资源会优先于系统配置使用
            sources.addBefore(RandomValuePropertySource.RANDOM_PROPERTY_SOURCE_NAME, MapPropertySource(it.name(), map))
        }

        eventSender?.refresh(this)
    }

    override fun setApplicationContext(applicationContext: ApplicationContext) {
        this.applicationContext = applicationContext
    }

    override fun setEnvironment(environment: Environment) {
        this.environment = environment
    }
}