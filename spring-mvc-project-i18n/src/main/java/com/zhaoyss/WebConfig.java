package com.zhaoyss;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.pebbletemplates.pebble.PebbleEngine;
import io.pebbletemplates.pebble.extension.AbstractExtension;
import io.pebbletemplates.pebble.extension.Extension;
import io.pebbletemplates.pebble.extension.Function;
import io.pebbletemplates.pebble.loader.Servlet5Loader;
import io.pebbletemplates.pebble.template.EvaluationContext;
import io.pebbletemplates.pebble.template.PebbleTemplate;
import io.pebbletemplates.spring.servlet.PebbleView;
import io.pebbletemplates.spring.servlet.PebbleViewResolver;
import jakarta.servlet.ServletContext;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.*;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;

import javax.sql.DataSource;
import java.io.File;
import java.util.*;

@Configuration
@ComponentScan
@EnableWebMvc // 启用 Spring MVC
@EnableTransactionManagement
@PropertySource("classpath:/jdbc.properties")
public class WebConfig {
    public static void main(String[] args) throws LifecycleException {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(Integer.getInteger("port", 8080));
        tomcat.getConnector();
        Context ctx = tomcat.addWebapp("", new File("src/main/webapp").getAbsolutePath());
        StandardRoot resources = new StandardRoot(ctx);
        resources.addPreResources(
                new DirResourceSet(resources, "/WEB-INF/classes", new File("target/classes").getAbsolutePath(), "/"));
        ctx.setResources(resources);
        tomcat.start();
        tomcat.getServer().await();
    }


    private Extension createExtension(MessageSource messageSource) {
        return new AbstractExtension() {
            @Override
            public Map<String, Function> getFunctions() {
                return Map.of("_", new Function() {
                    @Override
                    public List<String> getArgumentNames() {
                        return null;
                    }

                    @Override
                    public Object execute(Map<String, Object> args, PebbleTemplate self, EvaluationContext context, int lineNumber) {
                        String key = (String) args.get("0");
                        List<Object> arguments = this.extractArguments(args);
                        Locale locale = (Locale) context.getVariable("__locale__");
                        return messageSource.getMessage(key, arguments.toArray(), "???" + key + "???", locale);
                    }

                    private List<Object> extractArguments(Map<String, Object> args) {
                        int i = 1;
                        List<Object> arguments = new ArrayList<>();
                        while (args.containsKey(String.valueOf(i))) {
                            Object param = args.get(String.valueOf(i));
                            arguments.add(param);
                            i++;
                        }
                        return arguments;
                    }
                });
            }
        };
    }

    @Bean("i18n")
    MessageSource createMessageSource(){
        var messageSource = new ResourceBundleMessageSource();
        // 指定文件是UTF-8编码
        messageSource.setDefaultEncoding("UTF-8");
        // 指定主文件名
        messageSource.setBasename("message");
        return messageSource;
    }

    /**
     * 区域设置解析器
     */
    @Bean
    @Primary
    LocaleResolver createLocaleResolver(){
        var clr = new CookieLocaleResolver();
        clr.setDefaultLocale(Locale.ENGLISH);
        clr.setDefaultTimeZone(TimeZone.getDefault());
        return clr;
    }

    /**
     * Spring Mvc
     */
    @Bean
    WebMvcConfigurer createWebMvcConfigurer() {
        return new WebMvcConfigurer() {

            @Override
            public void addResourceHandlers(ResourceHandlerRegistry registry) {
                registry.addResourceHandler("/static/**").addResourceLocations("/static/");
            }
        };

    }

    /**
     * ViewResolver 视图解析器
     */
    @Bean
    ViewResolver createViewResolver(@Autowired ServletContext servletContext, @Autowired @Qualifier("i18n") MessageSource messageSource){
        var engine= new PebbleEngine.Builder()
                .autoEscaping(true)
                .cacheActive(false)
                .loader(new Servlet5Loader(servletContext))
                // 添加扩展
                .extension(createExtension(messageSource))
                .build();

        var viewResolver = new PebbleViewResolver(engine);
        viewResolver.setPrefix("/WEB-INF/templates/");
        viewResolver.setSuffix("");
        return viewResolver;
    }


    // -- jdbc configuration --------------------------------------------------

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