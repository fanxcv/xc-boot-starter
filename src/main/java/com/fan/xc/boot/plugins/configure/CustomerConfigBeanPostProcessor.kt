package com.fan.xc.boot.plugins.configure

import com.fan.xc.boot.starter.exception.XcToolsException
import com.fan.xc.boot.starter.processor.AbstractFieldValueBeanPostProcessor
import com.fan.xc.boot.starter.utils.Conversion
import org.springframework.beans.PropertyValues
import org.springframework.beans.TypeMismatchException
import org.springframework.beans.factory.BeanFactory
import org.springframework.beans.factory.BeanFactoryAware
import org.springframework.beans.factory.annotation.InjectionMetadata
import org.springframework.lang.NonNull
import org.springframework.lang.Nullable
import org.springframework.util.ReflectionUtils
import org.springframework.util.StringUtils
import java.lang.reflect.Field

/**
 * 用于注入属性和方法
 * @author fan
 * Create in 2020/1/10 09:56
 */
class CustomerConfigBeanPostProcessor : AbstractFieldValueBeanPostProcessor(), BeanFactoryAware {
    private lateinit var beanFactory: BeanFactory
    private lateinit var configs: ConfigureCache

    init {
        super.autowiredAnnotationTypes.add(InjectConfig::class.java)
    }

    override fun addElement(currElements: MutableList<InjectionMetadata.InjectedElement>, field: Field) {
        currElements.add(AutowiredFieldElement(field))
    }

    override fun setBeanFactory(beanFactory: BeanFactory) {
        this.configs = beanFactory.getBean(ConfigureCache::class.java)
        this.beanFactory = beanFactory
    }

    private inner class AutowiredFieldElement(field: Field) : InjectionMetadata.InjectedElement(field, null) {
        @Throws(Throwable::class)
        override fun inject(@NonNull bean: Any, @Nullable beanName: String, @Nullable pvs: PropertyValues) {
            val field = member as Field
            val annotation: InjectConfig = field.getAnnotation(InjectConfig::class.java)
            val name: String = if (StringUtils.hasText(annotation.value)) annotation.value else field.name
            var v = CustomerConfigUtils.convertValue(beanFactory, configs[name], annotation)
            // 参数转换
            if (v != null) {
                val clazz: Class<*> = field.type
                if (!clazz.isAssignableFrom(v.javaClass)) {
                    v = try {
                        Conversion.binder.convertIfNecessary(v, clazz, field)
                    } catch (ex: TypeMismatchException) {
                        throw XcToolsException("inject config fail", ex)
                    }
                }
            }
            if (v != null) {
                ReflectionUtils.makeAccessible(field)
                field[bean] = v
            }
        }
    }
}