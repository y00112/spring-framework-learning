package com.zhaoyss;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.pebbletemplates.pebble.PebbleEngine;
import io.pebbletemplates.pebble.loader.Servlet5Loader;
import io.pebbletemplates.spring.servlet.PebbleViewResolver;
import jakarta.servlet.ServletContext;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.sql.DataSource;
import java.io.File;
import java.util.Properties;

@Configuration
@ComponentScan
@EnableWebMvc
@EnableTransactionManagement
@PropertySource({"classpath:/jdbc.properties", "classpath:/smtp.properties"})
public class MailConfig {
    public static void main(String[] args) throws LifecycleException {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(Integer.getInteger("port", 8080));
        tomcat.getConnector();
        Context ctx = tomcat.addWebapp("", new File("src/main/webapp").getAbsolutePath());
        WebResourceRoot resources = new StandardRoot(ctx);
        resources.addPreResources(new DirResourceSet(resources, "/WEB-INF/classes", new File("target/classes").getAbsolutePath(), "/"));
        ctx.setResources(resources);
        tomcat.start();
        tomcat.getServer().await();
    }


    // -- Mvc configuration ---------------------------------------------------

    @Bean
    WebMvcConfigurer createWebMvcConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addResourceHandlers(ResourceHandlerRegistry registry) {
                registry.addResourceHandler("/static/**").addResourceLocations("/static/");
            }
        };
    }


    // -- pebble view configuration -------------------------------------------

    @Bean
    ViewResolver createViewResolver(@Autowired ServletContext servletContext) {
        var engine = new PebbleEngine.Builder().autoEscaping(true)
                // cache:
                .cacheActive(false)
                // loader:
                .loader(new Servlet5Loader(servletContext))
                // build:
                .build();
        var viewResolver = new PebbleViewResolver(engine);
        viewResolver.setPrefix("/WEB-INF/templates/");
        viewResolver.setSuffix("");
        return viewResolver;
    }
    @Bean
    JavaMailSender createJavaMailSender(
            @Value("${smtp.host}") String host,
            @Value("${smtp.port}") int port,
            @Value("${smtp.auth}") String auth,
            @Value("${smtp.username}") String username,
            @Value("${smtp.password}") String password,
            @Value("${smtp.debug:true}") String debug
    ){
        var mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setUsername(username);
        mailSender.setPassword(password);
        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol","smtp");
        props.put("mail.smtp.auth",auth);
        if (port == 587){
            props.put("mail.smtp.starttls.enable","true");
        }
        if (port == 465){
            props.put("mail.smtp.socketFactory.port", "465");
            props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
        }
        props.put("mail.debug",debug);
        return mailSender;
    }


    // ====================== JDBC ================
    @Value("${jdbc.url}")
    String jdbcUrl;

    @Value("${jdbc.username}")
    String jdbcUsername;

    @Value("${jdbc.password}")
    String jdbcPassword;

    @Bean
    DataSource createDataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(jdbcUsername);
        config.setPassword(jdbcPassword);
        config.addDataSourceProperty("autoCommit", "false");
        config.addDataSourceProperty("connectionTimeout", "5");
        config.addDataSourceProperty("idleTimeout", "60");
        return new HikariDataSource(config);
    }

    @Bean
    JdbcTemplate createJdbcTemplate(@Autowired DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    PlatformTransactionManager createTxManager(@Autowired DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}