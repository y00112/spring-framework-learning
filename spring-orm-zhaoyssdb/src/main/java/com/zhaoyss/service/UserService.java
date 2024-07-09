package com.zhaoyss.service;

import com.zhaoyss.entity.User;
import com.zhaoyss.orm.ZhaoyssdbTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class UserService {

    @Autowired
    ZhaoyssdbTemplate zhaoyssdbTemplate;

    public User register(String email, String password, String name) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setName(name);
        user.setCreatedAt(System.currentTimeMillis());
        zhaoyssdbTemplate.insert(user);
        return user;
    }
}
