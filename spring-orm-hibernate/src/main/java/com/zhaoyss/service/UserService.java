package com.zhaoyss.service;

import com.zhaoyss.entity.User;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Transactional
public class UserService {

    @Autowired
    SessionFactory sessionFactory;

    public User register(String email, String password, String name){
        // 创建一个User对象
        User user = new User();
        // 设置好各个属性
        user.setEmail(email);
        user.setPassword(password);
        user.setName(name);
        // 不设置id，因为使用了自增主键
        sessionFactory.getCurrentSession().persist(user);
        System.out.println(user.getId());
        return user;
    }

    public boolean deleteUser(Long id){
        User user = sessionFactory.getCurrentSession().byId(User.class).load(id);
        if (user != null){
            sessionFactory.getCurrentSession().remove(user);
            return true;
        }
        return false;
    }

    public void updateUser(Long id,String name){
        User user = sessionFactory.getCurrentSession().byId(User.class).load(id);
        user.setName(name);
        sessionFactory.getCurrentSession().merge(user);
    }

    public User login(String email, String password) {
        List<User> list = sessionFactory.getCurrentSession()
                .createNamedQuery("login", User.class) // 创建NamedQuery
                .setParameter("e", email) // 绑定e参数
                .setParameter("pwd", password) // 绑定pwd参数
                .list();
        return list.isEmpty() ? null : list.get(0);
    }
}
