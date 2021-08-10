package com.fan.xc.boot.plugins.api.gateway;

import com.fan.xc.boot.starter.event.Event;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;

/**
 * Gateway处理器接口
 * @author fan
 */
public interface XcGatewayHandler {

    /**
     * 校验请求是否符合预期
     * @param handler Spring处理器
     * @param request 当前的请求对象
     * @param event   参数对象
     * @return 如果为false, 会直接终止请求
     */
    boolean check(HandlerMethod handler, HttpServletRequest request, Event event);
}
