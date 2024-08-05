package com.zhaoyss.controller;

import com.zhaoyss.entity.User;
import com.zhaoyss.service.MailService;
import com.zhaoyss.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    MailService mailService;

    @RequestMapping("/register")
    public Long register(@RequestParam("email") String email,
                            @RequestParam("password") String password,
                            @RequestParam("name")String name){
        User user = userService.register(email, password, name);
        System.out.println("user registered: "  + user.getEmail());
        // send registration mail:
        new Thread(()->{
            mailService.sendRegistrationMail(user);
        }).start();
        return user.getId();
    }
}
