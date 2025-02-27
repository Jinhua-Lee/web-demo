package com.example.webdemo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * 测试RestController
 *
 * @author Jinhua-Lee
 * @version 1.0
 * @date 2022/2/11 22:47
 */
@RestController
@RequestMapping(value = "/demo-rest")
@Slf4j
public class DemoRestController {

    @GetMapping(value = "/hello")
    public String hello() {
        log.info("hello request accepted.");
        return "hello";
    }

    @GetMapping(value = "/hangup")
    public String hangup(@RequestParam("hangUpSeconds") Integer hangUpSeconds) throws InterruptedException {
        log.info("start to hang up.");
        TimeUnit.SECONDS.sleep(hangUpSeconds);
        log.info("hang up finished for {} seconds", hangUpSeconds);
        return "hangup";
    }
}
