package com.fan.xc.boot.starter.converters

import com.alibaba.fastjson.JSONArray
import org.springframework.core.convert.TypeDescriptor
import org.springframework.core.convert.converter.ConditionalGenericConverter
import org.springframework.core.convert.converter.GenericConverter.ConvertiblePair

/**
 * @author fan
 */
class JsonArray2ListGenericConverter : ConditionalGenericConverter {
    override fun matches(sourceType: TypeDescriptor, targetType: TypeDescriptor): Boolean {
        return true
    }

    override fun getConvertibleTypes(): Set<ConvertiblePair> {
        return setOf(ConvertiblePair(JSONArray::class.java, MutableList::class.java))
    }

    override fun convert(source: Any?, sourceType: TypeDescriptor, targetType: TypeDescriptor): Any? {
        if (source == null) return null

        val json = source as JSONArray
        val elementTypeDescriptor = targetType.elementTypeDescriptor
        return if (elementTypeDescriptor != null) {
            json.toJavaList(elementTypeDescriptor.type)
        } else {
            json
        }
    }
}