package com.zhaoyss.entity;

import jakarta.persistence.*;
@NamedQueries(
        @NamedQuery(
                name = "login",
                query = "SELECT u FROM User u WHERE u.email=:e AND u.password=:pwd"
        )
)
@Entity
@Table(name = "users")
public class User extends AbstractEntity{

    @Column(nullable = false,unique = true,length = 100)
    private String email;

    @Column(nullable = false,length = 100)
    private String password;

    @Column(nullable = false,length = 100)
    private String name;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
