package com.fan.xc.boot.plugins.swagger;

import com.fan.xc.boot.starter.configuration.XcConfiguration;
import com.fan.xc.boot.starter.event.Event;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.util.StringUtils;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Optional;

/**
 * @author fan
 */
@Slf4j
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "xc.swagger", value = "enable", havingValue = "true")
public class Swagger2Config implements ApplicationContextAware {
    private final XcConfiguration xcConfig;

    @Bean
    public Docket createRestApi() {
        log.info("===> init xc swagger2");
        XcConfiguration.SwaggerConfig config = xcConfig.getSwagger();
        return new Docket(DocumentationType.SWAGGER_2)
                .ignoredParameterTypes(Event.class)
                .ignoredParameterTypes(HttpServletRequest.class)
                .ignoredParameterTypes(HttpServletResponse.class)
                .apiInfo(new ApiInfoBuilder()
                        //设置文档的标题
                        .title(config.getTitle())
                        // 设置文档的版本信息-> 1.0.0 Version information
                        .version(config.getVersion())
                        // 设置文档的描述
                        .description(config.getDescription())
                        // 设置文档的License信息->1.3 License information
                        .termsOfServiceUrl(config.getTermsOfServiceUrl())
                        .build())
                .select()
                .apis(RequestHandlerSelectors.basePackage(Swagger2ImportBeanDefinitionRegistrar.packageName))
                // 可以根据url路径设置哪些请求加入文档，忽略哪些请求
                .paths(PathSelectors.any())
                .build();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        final Map<String, Object> beans = applicationContext.getBeansWithAnnotation(SpringBootApplication.class);
        Optional.of(beans.values().toArray()).map(it -> it[0])
                .map(it -> it.getClass().getPackage().getName())
                .ifPresent(it -> {
                    if (!StringUtils.hasText(Swagger2ImportBeanDefinitionRegistrar.packageName)) {
                        log.debug("==> swagger2 base package is: {}", it);
                        Swagger2ImportBeanDefinitionRegistrar.packageName = it;
                    }
                });
    }
}
