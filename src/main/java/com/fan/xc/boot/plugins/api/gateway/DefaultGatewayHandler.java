package com.fan.xc.boot.plugins.api.gateway;

import com.fan.xc.boot.plugins.api.gateway.chain.*;
import com.fan.xc.boot.starter.XcBootCoreAutoConfiguration;
import com.fan.xc.boot.starter.event.Event;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;

/**
 * 默认网关
 * IP黑名单校验 -> 限流 -> 请求时间间隔(依赖redis) -> 是否校验权限 -> 通过Token鉴权(依赖redis实现) -> IP白名单校验
 * @author fan
 */
@Slf4j
@Import({
        RollWindowRateLimitChain.class,
        IpBlackListCheckChain.class,
        IpWhiteListCheckChain.class,
        CheckTokenChain.class,
        TimeLimitChain.class,
        UnCheckChain.class
})
@Configuration
@AutoConfigureAfter(XcBootCoreAutoConfiguration.class)
@ConditionalOnBean(XcBootCoreAutoConfiguration.class)
@ConditionalOnProperty(prefix = "xc.gateway", value = "use-default-gateway", havingValue = "true")
public class DefaultGatewayHandler implements XcGatewayHandler, BeanFactoryAware, InitializingBean {
    private AbstractGatewayChain<ApiCheckData> gatewayChain;
    private BeanFactory beanFactory;

    @Data
    @AllArgsConstructor
    public static class ApiCheckData {
        private HttpServletRequest request;
        private HandlerMethod handler;
        private ApiCheck check;
        private String ip;

        public HttpServletRequest getRequest() {
            return request;
        }

        public HandlerMethod getHandler() {
            return handler;
        }

        public ApiCheck getCheck() {
            return check;
        }

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }
    }

    @Override
    public boolean check(HandlerMethod handler, HttpServletRequest request, Event event) {
        final ApiCheck check = handler.getMethodAnnotation(ApiCheck.class);
        final ApiCheckData data = new ApiCheckData(request, handler, check, null);

        final Boolean res = gatewayChain.exec(event, data);
        return res == null ? true : res;
    }

    @Override
    public void setBeanFactory(@NonNull BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Override
    public void afterPropertiesSet() {
        Assert.notNull(beanFactory, "beanFactory is must not be null");
        AbstractGatewayChain.Builder<ApiCheckData> builder = AbstractGatewayChain.builder();

        builder.addChain(beanFactory.getBean(IpBlackListCheckChain.class));

        try {
            builder.addChain(beanFactory.getBean(RollWindowRateLimitChain.class));
            builder.addChain(beanFactory.getBean(TimeLimitChain.class));
        } catch (NoSuchBeanDefinitionException e) {
            log.warn("no instance of TimeLimitChain, don't add it to gateway chain");
        }

        builder.addChain(beanFactory.getBean(UnCheckChain.class));

        try {
            builder.addChain(beanFactory.getBean(CheckTokenChain.class));
        } catch (NoSuchBeanDefinitionException e) {
            log.warn("no instance of CheckTokenChain, don't add it to gateway chain");
        }

        gatewayChain = builder.addChain(beanFactory.getBean(IpWhiteListCheckChain.class))
                .build();
        log.info("===> init default xc gateway chain: {}", gatewayChain.chain());
    }
}
