package com.zhaoyss;

import com.zhaoyss.service.User;
import com.zhaoyss.service.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {
    /**
     * Spring ioc容器就是ApplicationContext 他是一个接口，有很多实现类
     * 其中我们使用 ClassPathXmlApplicationContext 表示他会自动从 classpath 中查找指定的 xml 配置文件
     *
     * Spring 还提供了另一种ioc容器 BeanFactory 使用方式和 ApplicationContext 类似
     * BeanFactory factory = new XmlBeanFactory(new ClassPathResource("application.xml"));
     * MailService mailService = factory.getBean(MailService.class);
     *
     * BeanFactory 和 ApplicationContext 的区别在于 BeanFactory 的实现是按需创建的，即第一次获取Bean时才创建这个Bean，而ApplicationContext 会一次创建所以的Bean。
     * 实际上，ApplicationContext接口是从BeanFactory接口继承而来的，并且，ApplicationContext提供了一些额外的功能，包括国际化支持、事件和通知机制等。
     * 通常情况下，我们总是使用ApplicationContext，很少会考虑使用BeanFactory。
     *
     * @param args
     */
    public static void main(String[] args) {
        // 创建一个Spring的IOC容器，加载配置文件
        ApplicationContext context = new ClassPathXmlApplicationContext("application.xml");
        // 从容器中获取 bean
        UserService userService = context.getBean(UserService.class);
        // 正常调用
        User user = userService.login("bob@example.com", "password");
        System.out.println(user.getName());
    }
}