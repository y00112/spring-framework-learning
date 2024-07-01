package com.zhaoyss.service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 邮件服务
 * 在用于登录和注册成功后发送邮件通知
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MailService {
    @Autowired(required = false)
    ZoneId zoneId = ZoneId.systemDefault();

    public void setZoneId(ZoneId zoneId) {
        this.zoneId = zoneId;
    }

    public String getTime() {
        return ZonedDateTime.now(this.zoneId).format(DateTimeFormatter.ISO_ZONED_DATE_TIME);
    }

    public void sendLoginMail(User user) {
        System.err.println(String.format("Hi, %s! You are logged in at %s", user.getName(), getTime()));
    }

    public void sendRegistrationMail(User user) {
        System.err.println(String.format("Welcome, %s!", user.getName()));

    }


    /**
     * bean的初始化
     * 有些时候，Bean在注入必要的依赖后，需要进行初始化（监听消息等）
     */
    @PostConstruct
    public void init(){
        System.out.println("Init mail service with zoneId = " + this.zoneId);
    }

    /**
     * bean的销毁
     * 在容器关闭时，有时候还需要清理资源（关闭连接池等）
     */
    @PreDestroy
    public void shutDown(){
        System.out.println("Shutdown mail service");
    }
}
