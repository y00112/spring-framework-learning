package com.zhaoyss.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhaoyss.entity.MailMessage;
import com.zhaoyss.service.MessagingService;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.TextMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class MailMessageListener {


    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MessagingService messagingService;


    @JmsListener(destination = "jms/queue/mail", concurrency = "10")
    public void onMailMessageReceived(Message message) throws Exception {
        System.err.println("received message: " + message);
        if (message instanceof TextMessage) {
            String text = ((TextMessage) message).getText();
            MailMessage mm = objectMapper.readValue(text, MailMessage.class);
            System.err.println("objectMapper message: " + mm.toString());
        } else {
            System.err.println("unable to process non-text message!");
        }
    }
}
