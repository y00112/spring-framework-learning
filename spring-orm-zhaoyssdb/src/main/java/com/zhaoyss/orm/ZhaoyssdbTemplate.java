package com.zhaoyss.orm;

import jakarta.persistence.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class ZhaoyssdbTemplate {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private JdbcTemplate jdbcTemplate;

    public ZhaoyssdbTemplate(JdbcTemplate jdbcTemplate,String basePackage){
        this.jdbcTemplate = jdbcTemplate;
        List<Class<?>> classes = scanEntities(basePackage);

    }

    private List<Class<?>> scanEntities(String basePackage) {
        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter(new AnnotationTypeFilter(Entity.class));
        List<Class<?>> classes = new ArrayList<>();
        Set<BeanDefinition> beans = provider.findCandidateComponents(basePackage);
        for (BeanDefinition bean:beans){
            try {
                classes.add(Class.forName(bean.getBeanClassName()));
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        return classes;
    }

}
