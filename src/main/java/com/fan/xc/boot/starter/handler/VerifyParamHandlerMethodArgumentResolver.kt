package com.fan.xc.boot.starter.handler

import com.alibaba.fastjson.JSONArray
import com.fan.xc.boot.starter.annotation.VerifyParam
import com.fan.xc.boot.starter.event.EventImpl
import com.fan.xc.boot.starter.exception.ParamErrorException
import com.fan.xc.boot.starter.utils.Conversion
import org.springframework.beans.ConversionNotSupportedException
import org.springframework.beans.TypeMismatchException
import org.springframework.beans.factory.BeanFactory
import org.springframework.core.MethodParameter
import org.springframework.lang.NonNull
import org.springframework.util.StringUtils
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.annotation.MethodArgumentConversionNotSupportedException
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl
import java.lang.reflect.ParameterizedType
import java.util.*

/**
 * 注入相关参数
 */
class VerifyParamHandlerMethodArgumentResolver(beanFactory: BeanFactory) : BaseVerify(beanFactory), HandlerMethodArgumentResolver {
    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.hasParameterAnnotation(VerifyParam::class.java)
    }

    override fun resolveArgument(@NonNull parameter: MethodParameter,
                                 modelAndViewContainer: ModelAndViewContainer?,
                                 @NonNull webRequest: NativeWebRequest,
                                 binderFactory: WebDataBinderFactory?): Any? {
        val event = EventImpl.getEvent()

        val verify = parameter.getParameterAnnotation(VerifyParam::class.java)!!

        //获取参数名
        val name: Array<String> = verify.name

        if (name.size > 1) {
            throw ParamErrorException("name of VerifyParam can only have one value when in MethodParameter")
        }

        // VerifyParam拥有唯一注入对象，所以name只取第一个值
        val key: String = Optional.of(name)
                .map { if (it.isNotEmpty() && StringUtils.hasText(it[0])) it[0] else parameter.parameterName }
                .get()

        /*校验参数值，并做预处理*/
        var v = check(key, event.getParam(key), verify)

        val clazz = parameter.parameterType

        /*处理空参数*/
        v = handleNullValue(key, v, clazz)

        if (v == null) {
            return null
        }

        /*如果值是参数的子类，那就直接返回了*/
        if (clazz.isAssignableFrom(v.javaClass)) {
            //如果待注入对象为List，并且获取到值也为数组对象
            if (clazz == MutableList::class.java && v.javaClass == JSONArray::class.java) {
                val type: ParameterizedType = parameter.genericParameterType as ParameterizedType
                val nodeType = type.actualTypeArguments[0]
                //需要注入list的内部对象
                val nodeClazz: Class<*> = if (nodeType is ParameterizedTypeImpl) {
                    nodeType.rawType
                } else {
                    nodeType as Class<*>
                }
                if (nodeClazz != Any::class.java) {
                    v = (v as JSONArray).toJavaList(nodeClazz)
                }
            }
            return v
        }

        return try {
            Conversion.binder.convertIfNecessary(v, clazz, parameter)
        } catch (ex: ConversionNotSupportedException) {
            throw MethodArgumentConversionNotSupportedException(v, ex.requiredType,
                    key, parameter, ex.cause!!)
        } catch (ex: TypeMismatchException) {
            throw MethodArgumentTypeMismatchException(v, ex.requiredType,
                    key, parameter, ex.cause!!)
        }
    }

    private fun handleNullValue(name: String, value: Any?, paramType: Class<*>): Any? {
        if (value == null) {
            if (java.lang.Boolean.TYPE == paramType) {
                return java.lang.Boolean.FALSE
            } else if (paramType.isPrimitive) {
                throw ParamErrorException("Optional ${paramType.simpleName} parameter '$name" +
                        "' is present but cannot be translated into a null value due to being declared as a " +
                        "primitive type. Consider declaring it as object wrapper for the corresponding primitive type.")
            }
        }
        return value
    }
}