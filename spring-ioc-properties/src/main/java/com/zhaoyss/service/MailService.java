package com.zhaoyss.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MailService {

    @Value("#{smtpConfig.host}")
    private String smtpHost;

    @Value("#{smtpConfig.port}")
    private int smtpPort;

    @PostConstruct
    public void init() {
        System.out.println("smtpHost:" + smtpHost);
        System.out.println("smtpPort:" + smtpPort);
    }
}
