package com.zhaoyss.orm;

import jakarta.persistence.Id;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;
import java.util.Map;

public class Mapper<T> {

    private Class<T> entityClass;

    private String tableName;

    // @id property
    private BeanProperty id;
    // 包含 @id 在内的所有属性，key是属性的name，不是 column 的name
    private List<BeanProperty> allProperties;
    // 小写的属性名称
    private Map<String,BeanProperty> allPropertiesMap;
    private List<BeanProperty> insertableProperties;
    private List<BeanProperty> updatableProperties;
    private Map<String,BeanProperty> updatablePropertiesMap;
    private RowMapper<T> rowMapper;
    private String selectSQL;
    private String insertSQL;
    private String updateSQL;
    private String deleteSQL;
    public Mapper(Class<T> clazz){

    }
}
