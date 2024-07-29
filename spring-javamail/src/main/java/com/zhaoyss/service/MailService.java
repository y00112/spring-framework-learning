package com.zhaoyss.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MailService {

    @Value("${smtp.from}")
    String from;
}
