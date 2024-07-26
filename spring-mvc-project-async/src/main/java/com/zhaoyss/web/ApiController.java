package com.zhaoyss.web;

import com.zhaoyss.entity.User;
import com.zhaoyss.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

@RequestMapping("/api")
@RestController
public class ApiController {

    @Autowired
    private UserService userService;

    Logger logger = LoggerFactory.getLogger(getClass());

    @GetMapping("/version")
    public Map<String, String> version() {
        logger.info("get version...");
        return Map.of("version", "1.0");
    }
    @PostMapping("/register")
    public void register(@RequestParam("email") String email,@RequestParam("password") String password, @RequestParam("name") String name){
        User register = userService.register(email, password, name);
        System.out.println(register.getId());
    }

    /**
     *  async处理返回一个 Callable SpringMVC 自动把返回的 Callable放入线程池执行，等待结果后在写入相应
     */
    @GetMapping("/users")
    public Callable<List<User>> users() {
        logger.info("get users...");
        return () -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
            }
            logger.info("return users...");
            return userService.getUsers();
        };
    }
    /**
     *  使用 DeferredResult 时，可以设置超时，超时回自动返回超时错误相应。
     */

    @GetMapping("/users/{id}")
    public DeferredResult<User> user(@PathVariable("id") long id) {
        DeferredResult<User> result = new DeferredResult<>(3000L);
        new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
            try {
                User user = userService.getUserById(id);
                result.setResult(user);
                logger.info("deferred result is set.");
            } catch (Exception e) {
                result.setErrorResult(Map.of("error", e.getClass().getSimpleName(), "message", e.getMessage()));
                logger.warn("deferred error result is set.");
            }
        }).start();
        return result;
    }
}
