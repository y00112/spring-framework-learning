package com.zhaoyss.orm;


import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;

import java.beans.*;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;


public class Mapper<T> {

    final Class<T> entityClass;

    final String tableName;

    // @id property
    final BeanProperty id;
    // 包含 @id 在内的所有属性，key是属性的name，不是 column 的name
    final List<BeanProperty> allProperties;
    // 小写的属性名称
    final Map<String, BeanProperty> allPropertiesMap;
    final List<BeanProperty> insertableProperties;
    final List<BeanProperty> updatableProperties;
    private Map<String, BeanProperty> updatablePropertiesMap;
    final RowMapper<T> rowMapper;
    final String selectSQL;
    final String insertSQL;
    final String updateSQL;
    final String deleteSQL;

    public Mapper(Class<T> clazz) throws Exception {
        List<BeanProperty> all = getProperties(clazz);
        BeanProperty[] ids = all.stream().filter(BeanProperty::isId).toArray(BeanProperty[]::new);
        this.id = ids[0];
        this.allProperties = all;
        this.allPropertiesMap = buildPropertiesMap(this.allProperties);
        this.insertableProperties = all.stream().filter(BeanProperty::isInsertable).collect(Collectors.toList());
        this.updatableProperties = all.stream().filter(BeanProperty::isUpdatable).collect(Collectors.toList());
        this.entityClass = clazz;
        this.tableName = getTableName(clazz);

        this.selectSQL = "SELECT * FROM " + this.tableName + " WHERE " + this.id.columnName + " = ?";
        this.insertSQL = "INSERT INTO " + this.tableName + " ("
                + String.join(", ", this.insertableProperties.stream().map(p -> p.columnName).toArray(String[]::new)) + " ) VALUES ("
                + numOfQuestions(this.insertableProperties.size()) + ")";
        this.updateSQL = "UPDATE " + this.tableName + " SET "
                + String.join(", ", this.updatableProperties.stream().map(p -> p.columnName + " = ?").toArray(String[]::new)) + " WHERE " + this.id.columnName
                + " = ?";
        this.deleteSQL = "DELETE FROM " + this.tableName + " WHERE " + this.id.columnName + " = ?";
        this.rowMapper = new BeanPropertyRowMapper<>(this.entityClass);
    }

    Object getIdValue(Object bean) throws ReflectiveOperationException {
        return this.id.getter.invoke(bean);
    }

    private String numOfQuestions(int n) {
        String[] qs = new String[n];
        return String.join(", ", Arrays.stream(qs).map(s -> {
            return "?";
        }).toArray(String[]::new));
    }

    private String getTableName(Class<T> clazz) {
        Table table = clazz.getAnnotation(Table.class);
        if (table != null && !table.name().isEmpty()) {
            return table.name();
        }
        return clazz.getSimpleName();
    }

    private Map<String, BeanProperty> buildPropertiesMap(List<BeanProperty> props) {
        Map<String, BeanProperty> map = new HashMap<>();
        for (BeanProperty prop : props) {
            map.put(prop.propertyName.toLowerCase(), prop);
        }
        return map;
    }


    private List<BeanProperty> getProperties(Class<?> clazz) throws IntrospectionException {
        List<BeanProperty> properties = new ArrayList<>();
        BeanInfo info = Introspector.getBeanInfo(clazz);
        for (PropertyDescriptor pd : info.getPropertyDescriptors()) {
            // 排除getClass()
            if (pd.getName().equals("class")) {
                continue;
            }
            Method getter = pd.getReadMethod();
            Method setter = pd.getWriteMethod();
            // 忽略@Transient
            if (getter != null && getter.isAnnotationPresent(Transient.class)) {
                continue;
            }
            if (getter != null && setter != null) {
                properties.add(new BeanProperty(pd));
            } else {
                throw new RuntimeException("Property " + pd.getName() + " is not read/write.");
            }
        }
        return properties;
    }
}
