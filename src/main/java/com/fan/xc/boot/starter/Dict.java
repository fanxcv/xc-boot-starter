package com.fan.xc.boot.starter;

/**
 * @author fan
 */
public class Dict {
    public static final String REQUEST_DEAL_BY_XC_CORE = "xcCoreCoverThisRequest";
    /**
     * http请求头固定字段
     */
    public static final String HTTP_X_FORWARDED_FOR = "HTTP_X_FORWARDED_FOR";
    public static final String WL_PROXY_CLIENT_IP = "WL-Proxy-Client-IP";
    public static final String X_FORWARDED_FOR = "x-forwarded-for";
    public static final String PROXY_CLIENT_IP = "Proxy-Client-IP";
    public static final String HTTP_CLIENT_IP = "HTTP_CLIENT_IP";
    public static final String UNKNOWN = "unknown";

    public static final String BLANK = "";

    /**
     * 参数注解默认值
     */
    public static final String DEFAULT_VALUE = "\n\t\t\n\t\t\n\ue000\ue001\ue002\n\t\t\t\t\n";
    /**
     * Auth认证,保存token的redis前缀
     */
    public static final String AUTH_REDIS_PREFIX = "xc:gateway:auth:";
}
