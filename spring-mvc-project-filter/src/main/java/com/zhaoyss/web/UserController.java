package com.zhaoyss.web;

import com.zhaoyss.entity.User;
import com.zhaoyss.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    UserService userService;

    public static final String KEY_USER = "__user__";

    @PostMapping("/register")
    public User register(@RequestParam(value = "email") String email,
                         @RequestParam(value = "password") String password,
                         @RequestParam(value = "name") String name){
        User register = userService.register(email, password, name);
        return register;
    }

    @GetMapping("/users")
    public List<User> users() {
        return null;
    }

    @GetMapping("/users/{id}")
    public User user(@PathVariable("id") long id) {
        User user = userService.getUserById(id);
        return user;
    }

    @PostMapping("/signin")
    public Map<String, Object> signin(@RequestBody SignInRequest signinRequest) {
        try {
            User user = userService.signin(signinRequest.email, signinRequest.password);
            return Map.of("user", user);
        } catch (Exception e) {
            return Map.of("error", "SIGNIN_FAILED", "message", e.getMessage());
        }
    }

    public static class SignInRequest {
        public String email;
        public String password;
    }

    @GetMapping("/profile")
    public ModelAndView profile(HttpSession session) {
        User user = (User) session.getAttribute(KEY_USER);
        if (user == null) {
            return new ModelAndView("redirect:/signin");
        }
        return new ModelAndView("profile.html", Map.of("user", user));
    }
}
