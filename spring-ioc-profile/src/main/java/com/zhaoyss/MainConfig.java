package com.zhaoyss;

import com.zhaoyss.service.MailService;
import com.zhaoyss.service.ProfileService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;

import java.time.ZoneId;

@Configuration
@ComponentScan
public class MainConfig {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(MainConfig.class);
        ProfileService profileService = context.getBean(ProfileService.class);
    }

    /**
     * 知识点：
     * 在创建某个bean的时候，spring容器可以根据@Profile来决定是否创建。
     */
    @Bean
    @Profile("!test")
    ZoneId createZoneId() {
        return ZoneId.systemDefault();
    }

    @Bean
    @Profile({"test","master"}) // 满足test或者test,master
    ZoneId createZoneIdForTest() {
        return ZoneId.of("America/New_York");
    }


}