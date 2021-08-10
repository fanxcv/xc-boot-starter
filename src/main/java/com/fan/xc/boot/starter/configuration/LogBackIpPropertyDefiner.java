package com.fan.xc.boot.starter.configuration;

import ch.qos.logback.core.PropertyDefinerBase;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author yangfan323
 */
@Slf4j
public class LogBackIpPropertyDefiner extends PropertyDefinerBase {
    @Override
    public String getPropertyValue() {
        try {
            final InetAddress address = InetAddress.getLocalHost();
            return address.getHostAddress();
        } catch (UnknownHostException e) {
            log.error("获取服务器IP地址失败");
            return null;
        }
    }
}