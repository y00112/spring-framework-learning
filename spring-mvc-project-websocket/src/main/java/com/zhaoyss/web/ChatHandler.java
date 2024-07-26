package com.zhaoyss.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhaoyss.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class ChatHandler extends TextWebSocketHandler {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    ChatHistory chatHistory;

    // 保存所有 Client 的 WebSocket 会话实例
    private Map<String, WebSocketSession> clients = new ConcurrentHashMap<>();
    private AtomicInteger guestNumber = new AtomicInteger();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // 新会话根据ID放入Map
        clients.put(session.getId(), session);
        String name = null;
        User user = (User) session.getAttributes().get("__user__");
        if (user != null){
            name = user.getName();
        }else {
            name = initGuestName();
        }
        session.getAttributes().put("name",name);
        logger.info("websocket connection established: id = {}, name = {}", session.getId(), name);
        // 把历史消息发给新用户:
        List<ChatMessage> list = chatHistory.getHistory();
        session.sendMessage(toTextMessage(list));
        // 添加系统消息并广播:
        var msg = new ChatMessage("SYSTEM MESSAGE", name + " joined the room.");
        chatHistory.addToHistory(msg);
        broadcastMessage(msg);
    }

    private String initGuestName() {
        return "Guest" + this.guestNumber.incrementAndGet();
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        clients.remove(session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String s = message.getPayload().strip();
        if (s.isEmpty()) {
            return;
        }
        String name = (String) session.getAttributes().get("name");
        ChatText chat = objectMapper.readValue(s, ChatText.class);
        var msg = new ChatMessage(name,chat.text);
        chatHistory.addToHistory(msg);
        broadcastMessage(msg);
    }

    /**
     * 广播消息
     */
    public void broadcastMessage(ChatMessage chat) throws IOException {
        TextMessage message = toTextMessage(List.of(chat));
        for (String id : clients.keySet()) {
            WebSocketSession session = clients.get(id);
            session.sendMessage(message);
        }
    }

    private TextMessage toTextMessage(List<ChatMessage> messages) throws JsonProcessingException {
        String json = objectMapper.writeValueAsString(messages);
        return new TextMessage(json);
    }
}
