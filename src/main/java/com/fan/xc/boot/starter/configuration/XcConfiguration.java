package com.fan.xc.boot.starter.configuration;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author fan
 */
@Data
@Configuration
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties("xc")
public class XcConfiguration {
    /**
     * 核心配置
     */
    private CoreConfig core = new CoreConfig();

    private Config config;

    /**
     * 跨域配置
     */
    private CorsConfig cors = new CorsConfig();

    /**
     * 网关配置
     */
    private GatewayConfig gateway;

    /**
     * swagger配置
     */
    private SwaggerConfig swagger;

    @Data
    public static class SwaggerConfig {
        /**
         * 是否启用swagger
         */
        private boolean enable = false;
        /**
         * 版本号
         */
        private String version = "1.0.0";
        /**
         * 标题
         */
        private String title;
        /**
         * 描述
         */
        private String description;
        /**
         * 官网地址
         */
        private String termsOfServiceUrl;
    }

    @Data
    public static class CoreConfig {
        /**
         * core拦截路径
         */
        private List<String> path = Lists.newArrayList("/**");
        /**
         * core拦截器排除路径
         */
        private Set<String> excludePath = Sets.newHashSet("/error");

        /**
         * 是否启用core
         */
        private boolean enable = true;

        public List<String> getPath() {
            return path;
        }

        public List<String> getExcludePath() {
            return Lists.newArrayList(this.excludePath);
        }

        public void setExcludePath(List<String> excludePath) {
            this.excludePath.addAll(excludePath);
        }

        public boolean isEnable() {
            return enable;
        }
    }

    @Data
    public static class Config {
        /**
         * 远端配置文件地址
         */
        private Map<String, List<String>> remote;
    }

    @Data
    public static class CorsConfig {
        private transient List<String> any = Collections.singletonList("*");
        /**
         * Origin白名单
         */
        private List<String> allowedOrigins = any;
        /**
         * Header白名单
         */
        private List<String> allowedHeaders = any;
        /**
         * Method白名单
         */
        private List<String> allowedMethods = any;
    }

    @Data
    public static class GatewayConfig {
        /**
         * 是否加载默认网关
         */
        private boolean useDefaultGateway = false;

        /**
         * checkToken关联的Cookie Key
         */
        private String authTokenKey = "IFIS_AUTH";

        /**
         * IP校验配置
         */
        private IpCheckConfig ip;

        public boolean isUseDefaultGateway() {
            return useDefaultGateway;
        }

        public String getAuthTokenKey() {
            return authTokenKey;
        }

        public IpCheckConfig getIp() {
            return ip;
        }
    }

    @Data
    public static class IpCheckConfig {
        /**
         * 白名单, 支持细化配置
         */
        private Map<String, List<String>> white;
        /**
         * 黑名单, 仅支持全局生效
         */
        private List<String> black;

        public Map<String, List<String>> getWhite() {
            return white;
        }

        public List<String> getBlack() {
            return black;
        }
    }

    public CoreConfig getCore() {
        return core;
    }

    public Config getConfig() {
        return config;
    }

    public GatewayConfig getGateway() {
        return gateway;
    }

    public XcConfiguration copy() {
        final XcConfiguration configuration = new XcConfiguration();
        configuration.setGateway(gateway);
        configuration.setCors(cors);
        return configuration;
    }
}
