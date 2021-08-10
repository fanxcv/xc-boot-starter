package com.fan.xc.boot.plugins.weixin

import org.springframework.beans.factory.support.BeanDefinitionRegistry
import org.springframework.beans.factory.support.RootBeanDefinition
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar
import org.springframework.core.type.AnnotationMetadata

/**
 * 注册相关Bean
 * @author fan
 */
open class WeiXinRegistrar : ImportBeanDefinitionRegistrar {

    override fun registerBeanDefinitions(metadata: AnnotationMetadata, registry: BeanDefinitionRegistry) {
        val attributes = metadata.getAnnotationAttributes(EnableWeiXinApi::class.java.name)
        WeiXinConfig.syncFromApi = attributes?.get("fromApi") as Boolean

        val accessTokenManagerClazz = AccessTokenManager::class.java
        val jsApiTicketManagerClazz = JsApiTicketManager::class.java
        registry.registerBeanDefinition(accessTokenManagerClazz.name, RootBeanDefinition(accessTokenManagerClazz))
        registry.registerBeanDefinition(jsApiTicketManagerClazz.name, RootBeanDefinition(jsApiTicketManagerClazz))
    }
}