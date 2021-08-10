package com.fan.xc.boot.plugins.api.cache;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

/**
 * 通过AOP实现公共缓存方案
 * @author fan
 */
@Slf4j
@Aspect
@RequiredArgsConstructor
public class CacheAop {
    private final CacheUtils cache;

    @Around("@annotation(com.fan.xc.boot.plugins.api.cache.XCCache)")
    public Object doAround(ProceedingJoinPoint point) throws Throwable {
        return cache.doAround(point);
    }
}