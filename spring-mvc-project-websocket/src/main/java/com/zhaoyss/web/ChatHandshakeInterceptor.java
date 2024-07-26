package com.zhaoyss.web;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.util.List;

@Component
public class ChatHandshakeInterceptor extends HttpSessionHandshakeInterceptor {

    public ChatHandshakeInterceptor(){
        // 指定从 HttpSession 复制属性到 WebSocketSession
        super(List.of(UserController.KEY_USER));
    }

}
