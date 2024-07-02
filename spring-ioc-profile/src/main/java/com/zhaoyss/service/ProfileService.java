package com.zhaoyss.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.ZoneId;

@Component
public class ProfileService {

    @Autowired(required = false)
    ZoneId zoneId = ZoneId.systemDefault();

    public void setZoneId(ZoneId zoneId) {
        this.zoneId = zoneId;
    }

    @PostConstruct
    public void init(){
        System.out.println("Init mail service with zoneId = " + this.zoneId);
    }
}
