package com.zhaoyss;


import com.zhaoyss.entity.User;
import com.zhaoyss.service.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan
@EnableTransactionManagement
@PropertySource("jdbc.properties")
public class Main {
    public static void main(String[] args) {

        ApplicationContext context = new AnnotationConfigApplicationContext(Main.class);
        UserService userService = context.getBean(UserService.class);
        User user = new User();
        // 查
        if (userService.selectUserByEmail("zhaoyss@qq.com") == null){
            // 增
            user = userService.register("zhaoyss@qq.com", "password", "zhaoyss");
            System.err.println("userService.register(): " + user.getName());
        }
        // 改
        userService.updateUser(user.getId(),"zhaoyss-update");
        System.err.println("userService.updateUser(): ");
        // 查
        user = userService.getUserById(user.getId());
        System.err.println("userService.getUserById(): " + user.getName());
        // 删
        // userService.deleteUser(12L);

    }}