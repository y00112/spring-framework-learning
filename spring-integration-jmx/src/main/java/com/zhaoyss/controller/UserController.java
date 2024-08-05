package com.zhaoyss.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zhaoyss.entity.User;
import com.zhaoyss.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {


    @Autowired
    UserService userService;


    @PostMapping("/register")
    public Long register(@RequestParam("email") String email, @RequestParam("name") String name, @RequestParam("password") String password) throws JsonProcessingException {
        User register = userService.register(email, password, name);
        return register.getId();
    }

}
