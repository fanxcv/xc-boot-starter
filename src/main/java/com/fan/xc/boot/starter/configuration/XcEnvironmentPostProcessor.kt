package com.fan.xc.boot.starter.configuration

import lombok.SneakyThrows
import org.springframework.boot.SpringApplication
import org.springframework.boot.context.event.ApplicationPreparedEvent
import org.springframework.boot.context.properties.bind.Binder
import org.springframework.boot.env.EnvironmentPostProcessor
import org.springframework.boot.env.PropertySourceLoader
import org.springframework.boot.env.RandomValuePropertySource
import org.springframework.boot.env.YamlPropertySourceLoader
import org.springframework.boot.logging.DeferredLog
import org.springframework.context.ApplicationListener
import org.springframework.core.Ordered
import org.springframework.core.env.ConfigurableEnvironment
import org.springframework.core.env.MutablePropertySources
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import java.util.*

/**
 * 用于项目启动前加载服务器上的配置文件
 * @author fan
 */
class XcEnvironmentPostProcessor : EnvironmentPostProcessor, Ordered, ApplicationListener<ApplicationPreparedEvent> {
    companion object {
        // 用于日志缓存并在合适时间打印出来
        private val xcLogger = DeferredLog()
    }

    // 先初始化一个yml的解析器
    private val loader: PropertySourceLoader = YamlPropertySourceLoader()

    /**
     * The default order for the processor.
     */
    override fun getOrder(): Int = 0

    @SneakyThrows
    override fun postProcessEnvironment(environment: ConfigurableEnvironment, application: SpringApplication) {
        val binder = Binder.get(environment)
        val remoteUri: Array<String> = binder.bind("xc.config.remote", Array<String>::class.java).orElse(arrayOf())
        remoteUri.forEachIndexed { index, item -> loadProperties("defaultXcRemoteConfigure$index", item, environment.propertySources) }
        // 先从配置文件中读取远程配置路径
        environment.activeProfiles.forEach {
            val uri: Array<String> = binder.bind("xc.config.remote.$it", Array<String>::class.java).orElse(arrayOf())
            // 如果有相关的配置,依次加载
            uri.forEachIndexed { index, item -> loadProperties("${it}XcRemoteConfigure$index", item, environment.propertySources) }
        }
    }

    /**
     * 加载远程配置
     */
    private fun loadProperties(name: String, url: String, destination: MutablePropertySources) {
        val resource: Resource = UrlResource(url)
        if (resource.exists()) {
            // 如果资源存在的话,使用PropertySourceLoader加载配置文件
            val load = loader.load(name, resource)
            // 将对应的资源放在RandomValuePropertySource前面,保证加载的远程资源会优先于系统配置使用
            load?.forEach { destination.addBefore(RandomValuePropertySource.RANDOM_PROPERTY_SOURCE_NAME, it) }
            xcLogger.info("load configuration success from $url")
        } else {
            xcLogger.error("get configuration fail from $url, don't load this")
        }
    }

    override fun onApplicationEvent(event: ApplicationPreparedEvent) {
        // 打印日志
        xcLogger.replayTo(XcEnvironmentPostProcessor::class.java)
    }
}