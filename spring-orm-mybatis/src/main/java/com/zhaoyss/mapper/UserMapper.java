package com.zhaoyss.mapper;

import com.zhaoyss.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface UserMapper {

    @Select("SELECT * FROM users WhERE id = #{id}")
    User getById(@Param("id") long id);

    @Select("SELECT * FROM users LIMIT #{offset}, #{maxResults}")
    List<User> getAll(@Param("offset") int offset, @Param("maxResults") int maxResults);

    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    @Insert("INSERT INTO users (email, password, name, createdAt) VALUES (#{user.email}, #{user.password}, #{user.name}, #{user.createdAt})")
    void insert(@Param("user") User user);

    @Update("UPDATE users SET name = #{user.name}, createdAt = #{user.createdAt} WHERE id = #{user.id}")
    void update(@Param("user") User user);

    @Delete("DELETE FROM users WHERE id = #{id}")
    void deleteById(@Param("id") long id);

}
