package com.fan.xc.boot.starter.interfaces;

import org.springframework.web.method.support.HandlerMethodArgumentResolver;

/**
 * XC扩展HandlerMethodArgumentResolver
 * 实现该接口的参数解析器会添加到默认参数解析器的前面
 * @author fan
 */
public interface XcHandlerMethodArgumentResolver extends HandlerMethodArgumentResolver {
}
