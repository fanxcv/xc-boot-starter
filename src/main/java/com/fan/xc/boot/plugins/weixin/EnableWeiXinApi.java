package com.fan.xc.boot.plugins.weixin;

import com.fan.xc.boot.plugins.weixin.grpc.WeiXinRpcClient;
import com.fan.xc.boot.plugins.weixin.grpc.WeiXinRpcServer;
import com.fan.xc.boot.starter.configuration.XcConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author fan
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({
        WeiXinRegistrar.class,
        WeiXinRpcClient.class,
        WeiXinRpcServer.class,
        WeiXinController.class,
        WeiXinApi.class,
        XcConfiguration.class
})
@EnableConfigurationProperties(WeiXinConfig.class)
public @interface EnableWeiXinApi {
}
