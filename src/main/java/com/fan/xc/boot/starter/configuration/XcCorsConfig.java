package com.fan.xc.boot.starter.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.LegacyCookieProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @author fan
 */
@Slf4j
@Order
@Configuration
@RequiredArgsConstructor
public class XcCorsConfig {
    private final XcConfiguration xcConfig;

    @Bean
    @ConditionalOnMissingBean(CorsFilter.class)
    public CorsFilter corsFilter() {
        log.info("===> init xc cors filter");
        XcConfiguration.CorsConfig config = xcConfig.getCors();
        log.info("origins allowed {}", config.getAllowedOrigins());
        log.info("headers allowed {}", config.getAllowedHeaders());
        log.info("methods allowed {}", config.getAllowedMethods());
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOriginPatterns(config.getAllowedOrigins());
        corsConfiguration.setAllowedHeaders(config.getAllowedHeaders());
        corsConfiguration.setAllowedMethods(config.getAllowedMethods());
        //接受cookie
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        xcConfig.getCore().getPath().forEach(it -> source.registerCorsConfiguration(it, corsConfiguration));
        return new CorsFilter(source);
    }

    @Bean
    @ConditionalOnMissingBean(WebServerFactoryCustomizer.class)
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> cookieProcessorCustomizer() {
        log.info("===> init xc cookie processor customizer");
        return (factory) -> factory.addContextCustomizers(
                (context) -> context.setCookieProcessor(new LegacyCookieProcessor()));
    }
}
