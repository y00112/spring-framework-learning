package com.zhaoyss.service;

import com.zhaoyss.entity.User;
import com.zhaoyss.mapper.UserMapper;
import com.zhaoyss.orm.ZhaoyssdbTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class UserService {
    @Autowired
    UserMapper userMapper;

    public User getUserById(long id){
        // 调用Mapper方法:
        User user = userMapper.getById(id);
        if (user == null) {
            throw new RuntimeException("User not found by id.");
        }
        return user;
    }

    public User register(String email,String password,String name){
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        userMapper.insert(user);
        return user;
    }
}
