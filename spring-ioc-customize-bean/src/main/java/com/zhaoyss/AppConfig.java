package com.zhaoyss;

import com.zhaoyss.service.User;
import com.zhaoyss.service.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;

import java.time.ZoneId;

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
        System.out.println(user.getName());
    }

    /**
     * 第三方bean的创建
     * Spring对标记为 @Bean 的方法只调用一次，因此返回的Bean仍然是单例。
     *
     * bean的别名：默认情况下，容器只会创建一个实例。但是有些时候，我们同一种类型的bean创建多个实例。
     * 例如同时连接多个数据库，就必须创建多个 DataSource实例。
     * 可以使用bean别名的形式来区分同一类型不同实例 @Bean("Z“） @Bean("utc8") 也可以使用 @Bean + @Qualifier("Z") 指定别名。
     * 否则会抛出异常：NoUniqueBeanDefinitionException，意思出现了重复的Bean定义。
     *
     * @Primary
     * 如果不想使用别名的形式来区分同一类型不同实例的Bean，则可以将主数据定义为 @Primary。
     *
     * @return
     */
    @Bean
    @Primary
    ZoneId createZoneIdOfZ() {
        return ZoneId.of("Z");
    }

    @Bean
    ZoneId createZoneOfUTC8(){
        return ZoneId.of("UTC+08:00");
    }

}
