package com.fan.xc.boot.starter.interfaces;

import com.fan.xc.boot.starter.event.Event;

import javax.servlet.http.HttpServletRequest;

/**
 * Core 对外暴露接口
 * @author fan
 */
public interface XcEventInterface {

    /**
     * 从请求中解析Token
     * @param request 当前请求对象
     * @return token
     */
    default String parseToken(HttpServletRequest request) {
        return null;
    }

    /**
     * 请求完成后回调
     * @param event 参数对象
     */
    default void afterCompletion(Event event) {
    }
}
