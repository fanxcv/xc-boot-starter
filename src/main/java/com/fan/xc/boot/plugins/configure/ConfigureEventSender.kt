package com.fan.xc.boot.plugins.configure

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.cloud.endpoint.event.RefreshEvent
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware

/**
 * @author fan
 */
@ConditionalOnClass(RefreshEvent::class)
class ConfigureEventSender : ApplicationContextAware {
    private lateinit var applicationContext: ApplicationContext

    fun refresh(source: Any) {
        applicationContext.publishEvent(RefreshEvent(source, null, "Refresh custom config"))
    }

    override fun setApplicationContext(applicationContext: ApplicationContext) {
        this.applicationContext = applicationContext
    }
}