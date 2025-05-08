package com.example.webdemo.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author Jinhua-Lee
 */
@Slf4j
@Component
@ConditionalOnProperty(prefix = "task", name = "enable", havingValue = "true")
public class MyTask {

    @Scheduled(fixedDelay = 500)
    public void run() {
        log.info("MyTask run...");
    }
}
