package com.zhaoyss.service;

import com.zhaoyss.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Transactional

public class UserService {

    @PersistenceContext
    EntityManager em;

    public User getUserById(long id) {
        User user = this.em.find(User.class, id);
        if (user == null) {
            throw new RuntimeException("User not found by id: " + id);
        }
        return user;
    }

    public User fetchUserByEmail(String email) {
        // JPQL查询:
        TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.email = :e", User.class);
        query.setParameter("e", email);
        List<User> list = query.getResultList();
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    public User login(String email, String password) {
        TypedQuery<User> query = em.createNamedQuery("login", User.class);
        query.setParameter("e", email);
        query.setParameter("pwd", password);
        List<User> list = query.getResultList();
        return list.isEmpty() ? null : list.get(0);
    }

    public void register(String mail, String password, String name) {
        User user = new User();
        user.setEmail(mail);
        user.setPassword(password);
        user.setName(name);
        this.em.persist(user);
    }
}
