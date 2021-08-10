package com.fan.xc.boot.plugins.configure

import org.springframework.beans.factory.support.BeanDefinitionRegistry
import org.springframework.beans.factory.support.RootBeanDefinition
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar
import org.springframework.core.type.AnnotationMetadata

open class CustomerConfigConfiguration : ImportBeanDefinitionRegistrar {

    /**
     * 注册CustomerConfigBeanPostProcessor后置处理器，用于后续完成InjectConfig的功能
     * @author fan
     */
    override fun registerBeanDefinitions(importingClassMetadata: AnnotationMetadata, registry: BeanDefinitionRegistry) {
        val attributes: MutableMap<String, Any> = importingClassMetadata.getAnnotationAttributes(EnableCustomerConfig::class.java.name)!!
        /*数据缓存容器*/
        val cache = attributes["value"] as Class<*>

        registry.registerBeanDefinition(cache.name, RootBeanDefinition(cache))
    }
}