package com.zhaoyss.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TaskService {

    @Scheduled(initialDelay = 60_000, fixedRate = 60_000)
    public void checkSystemStatusEveryMinute() {
        System.err.println("Start check system status");
    }

    /**
     * cron
     */
    @Scheduled(cron = "0 15 2 * * *")
    public void cronDailyReport(){
        System.err.println("Start daily report task...");
    }
}
