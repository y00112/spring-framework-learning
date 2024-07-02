package com.zhaoyss;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;

import java.time.ZoneId;

@Configuration
@ComponentScan
@PropertySource("app.properties") // 表示读取classpath的app。properties
public class AppConfig {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
    }

    /**
     * 知识点：
     * "${app.zone}"表示读取key为app.zone的value，如果key不存在，启动将报错。
     * "${app.zone:Z}"表示读取key为app.zone的value，但如果key不存在，就使用默认值Z。
     */
    @Value("${app.zone:Z}")
    String zoneId;

    @Bean
    ZoneId createZoneId(){
        return ZoneId.of(zoneId);
    }
}
