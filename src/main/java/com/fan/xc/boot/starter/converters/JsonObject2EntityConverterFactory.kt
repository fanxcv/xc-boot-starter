package com.fan.xc.boot.starter.converters

import com.alibaba.fastjson.JSONObject
import org.springframework.core.convert.converter.Converter
import org.springframework.core.convert.converter.ConverterFactory

/**
 * @author fan
 */
class JsonObject2EntityConverterFactory : ConverterFactory<JSONObject, Any?> {
    override fun <T> getConverter(targetType: Class<T>): Converter<JSONObject, T> {
        return JsonObject2EntityConverter(targetType)
    }

    internal class JsonObject2EntityConverter<D>(private val targetType: Class<D>) : Converter<JSONObject, D> {
        override fun convert(source: JSONObject): D {
            return source.toJavaObject(targetType)
        }
    }
}