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
     * 同步Token的URL地址
     */
    private String syncPath;
    /**
     * 微信APP ID
     */
    private String appId = Dict.BLANK;
    /**
     * 微信APP Secret
     */
    private String appSecret = Dict.BLANK;
    /**
     * 接口调用凭证
     */
    private String authorization = "authorization";
    /**
     * 是否从远端同步Token
     */
    static boolean syncFromApi = false;

    public String getToken() {
        return token;
    }

    public String getSyncPath() {
        return syncPath;
    }

    public String getAppId() {
        return appId;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public String getAuthorization() {
        return authorization;
    }

    public boolean isSyncFromApi() {
        return syncFromApi;
    }
}
