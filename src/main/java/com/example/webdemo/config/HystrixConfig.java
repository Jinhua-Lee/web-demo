package com.example.webdemo.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Configuration;

/**
 * Hystrix启用的开关配置，保证不影响已有的项目
 *
 * @author Jinhua
 */
@Configuration
@EnableHystrix
@ConditionalOnProperty(prefix = "hystrix", name = "enabled", havingValue = "true")
public class HystrixConfig {
}
