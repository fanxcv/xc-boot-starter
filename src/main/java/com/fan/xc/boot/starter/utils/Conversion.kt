package com.fan.xc.boot.starter.utils

import org.springframework.beans.factory.BeanFactory
import org.springframework.beans.factory.BeanFactoryAware
import org.springframework.boot.autoconfigure.web.format.WebConversionService
import org.springframework.stereotype.Component
import org.springframework.util.Assert
import org.springframework.validation.DataBinder

/**
 * 基于Spring DataBinder的类型转换实现
 */
@Component
class Conversion : BeanFactoryAware {
    override fun setBeanFactory(beanFactory: BeanFactory) {
        Companion.beanFactory = beanFactory
    }

    companion object {
        private lateinit var beanFactory: BeanFactory

        @JvmStatic
        val binder: DataBinder by lazy {
            Assert.notNull(beanFactory, "BeanFactory is null, init binder fail")
            val dataBinder = DataBinder(null, this::class.java.simpleName)
            dataBinder.conversionService = beanFactory.getBean(WebConversionService::class.java)
            dataBinder
        }
    }
}