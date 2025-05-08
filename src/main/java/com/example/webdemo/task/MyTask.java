package com.example.webdemo.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author Jinhua-Lee
 */
@Slf4j
@Component
@ConditionalOnProperty(prefix = "task", name = "enable", havingValue = "true")
public class MyTask {

    @Scheduled(fixedDelay = 500)
    public void run() {
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        log.info("MyTask run...");
    }
}
