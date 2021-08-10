package com.fan.xc.boot.plugins.configure

import org.springframework.beans.factory.BeanFactory
import org.springframework.beans.factory.BeanFactoryAware
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

open class Configure : WebMvcConfigurer, BeanFactoryAware {
    private lateinit var beanFactory: BeanFactory

    /**
     * 注册CustomerConfigArgumentResolver，用于支持在MVC Controller中注入自定义参数
     */
    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        // 此处直接吧ConfigureCache实例注入，方便后面获取参数值
        resolvers.add(CustomerConfigArgumentResolver(beanFactory.getBean(ConfigureCache::class.java), beanFactory))
    }

    override fun setBeanFactory(beanFactory: BeanFactory) {
        this.beanFactory = beanFactory
    }
}