package com.fan.xc.boot.starter.converters

import com.alibaba.fastjson.JSONObject
import org.springframework.core.convert.TypeDescriptor
import org.springframework.core.convert.converter.ConditionalGenericConverter
import org.springframework.core.convert.converter.GenericConverter.ConvertiblePair

/**
 * @author fan
 */
class String2EntityConverterFactory : ConditionalGenericConverter {
    override fun getConvertibleTypes(): Set<ConvertiblePair> {
        return setOf(ConvertiblePair(String::class.java, Any::class.java))
    }

    override fun convert(source: Any?, sourceType: TypeDescriptor, targetType: TypeDescriptor): Any? {
        return if (source == null) null else JSONObject.parseObject(source as String, targetType.type)
    }

    override fun matches(sourceType: TypeDescriptor, targetType: TypeDescriptor): Boolean {
        // 该转换器不适用于转换为List
        return targetType.type != MutableList::class.java && targetType.type != sourceType.type
    }
}