package com.zhaoyss;

import com.zhaoyss.service.User;
import com.zhaoyss.service.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 使用 AnnotationConfigApplicationContext 必须传入一个标注了 @Configuration 的类
 * @ComponentScan 他告诉容器，自动搜索当前类所在的包以及子包，把所以标注为 @Component 的Bean自动创建出来，并根据 @Autowired进行装配
 */
@Configuration
@ComponentScan
public class AppConfig {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        UserService userService = context.getBean(UserService.class);
        User user = userService.login("bob@example.com", "password");
        System.out.println(user.getName());    }
}
