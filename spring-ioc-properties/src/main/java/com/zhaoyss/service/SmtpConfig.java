package com.zhaoyss.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 知识点：
 * 通过一个JavaBean-》SmtpConfig读取所有的配置
 * 在需要用到的地方，使用#{smtpConfig.host}注入
 * ${key} 表示从配置文件中读取key的value
 * #{bean.host} 表示从JavaBean中读取读取属性的值
 *
 */
@Component
public class SmtpConfig {

    @Value("${smtp.host}")
    private String host;

    @Value("${smtp.port:25}")
    private int port;

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }
}
