package com.fan.xc.boot.plugins.weixin;

import com.fan.xc.boot.starter.Dict;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author yangfan323
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "xc.weixin")
public class WeiXinConfig {
    /**
     * 微信服务器配置用到的Token
     */
    private String token;
    /**
     * 微信APP ID
     */
    private String appId = Dict.BLANK;
    /**
     * 微信APP Secret
     */
    private String appSecret = Dict.BLANK;
    /**
     * 服务端配置
     */
    private Server server;
    /**
     * 客户端配置
     */
    private Client client;

    @Data
    public static class Server {
        /**
         * 是否启用服务端
         */
        private boolean enable = false;
        /**
         * 监听端口
         */
        private int port = 20002;

        public boolean isEnable() {
            return enable;
        }

        public int getPort() {
            return port;
        }
    }

    @Data
    public static class Client {
        /**
         * 是否启用客户端
         */
        private boolean enable = false;
        /**
         * Server端IP
         */
        private String host;
        /**
         * Server端端口
         */
        private int port = 20002;

        public boolean isEnable() {
            return enable;
        }

        public String getHost() {
            return host;
        }

        public int getPort() {
            return port;
        }
    }

    public Server getServer() {
        return server;
    }

    public Client getClient() {
        return client;
    }

    public String getToken() {
        return token;
    }

    public String getAppId() {
        return appId;
    }

    public String getAppSecret() {
        return appSecret;
    }
}
