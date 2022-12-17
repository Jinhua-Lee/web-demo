package com.example.webdemo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试RestController
 *
 * @author Jinhua-Lee
 * @version 1.0
 * @date 2022/2/11 22:47
 */
@RestController
@RequestMapping(value = "demo")
@Slf4j
public class DemoRestController {

    @GetMapping(value = "hello")
    public String hello() {
        if (log.isInfoEnabled()) {
            log.info("hello request accepted.");
        }
        return "hello";
    }
}
