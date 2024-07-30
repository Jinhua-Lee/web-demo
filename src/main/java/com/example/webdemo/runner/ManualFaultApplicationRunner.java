package com.example.webdemo.runner;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * 模拟手动异常
 *
 * @author Jinhua-Lee
 */
@Slf4j
@Component
@ConditionalOnProperty(prefix = "manual.fault", name = "enable", havingValue = "true")
public class ManualFaultApplicationRunner implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) {
        log.info("ManualFaultApplicationRunner: application exit.");
        System.exit(-1);
    }
}
