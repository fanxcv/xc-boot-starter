package com.xc.fan.starter.utils;

import com.fan.xc.boot.starter.utils.NetUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
public class Test {
    public String test() {
        return NetUtils.get("http://www.baidu.com", (u, connection) -> {
            connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            try (InputStream is = connection.getInputStream()) {
                // TODO
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
            return "返回信息";
        });
    }
}
