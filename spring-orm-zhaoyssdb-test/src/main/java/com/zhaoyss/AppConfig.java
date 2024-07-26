package com.zhaoyss;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import com.zhaoyss.entity.AbstractEntity;
import com.zhaoyss.entity.User;
import com.zhaoyss.service.UserService;
import io.github.y00112.DbConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@ComponentScan
@EnableTransactionManagement
@Import(DbConfig.class)
public class AppConfig {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
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

    }
}