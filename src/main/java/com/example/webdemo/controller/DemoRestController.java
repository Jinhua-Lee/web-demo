package com.example.webdemo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

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

    @SuppressWarnings("all")
    private final List<byte[]> dataList = new ArrayList<>();
    private Integer totalMegaBytes = 0;

    private final AtomicInteger threadCount = new AtomicInteger();

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

    @GetMapping(value = "/memory-out")
    public Integer memoryOut(@RequestParam(value = "addMegaBytes") Integer addMegaBytes) {
        addMegaBytes = addMegaBytes <= 0 ? 100 : addMegaBytes;
        log.info("start to memory out. total memory = {} mb.", totalMegaBytes);
        try {
            synchronized (dataList) {
                dataList.add(new byte[addMegaBytes * 1024 * 1024]);
                totalMegaBytes += addMegaBytes;
            }
        } catch (OutOfMemoryError oom) {
            log.error("memory out error.", oom);
            throw oom;
        }
        log.info("memory out finished. total memory = {} mb.", totalMegaBytes);
        return totalMegaBytes;
    }

    @GetMapping(value = "/thread-oom")
    @SuppressWarnings("all")
    public Integer threadOom() {
        while(true) {
            log.info("[thread oom] thread count = {}", threadCount.getAndIncrement());
            new Thread(() -> {
                try {
                    TimeUnit.SECONDS.sleep(1_000);
                } catch (InterruptedException ignored) {
                }
            }).start();
        }
    }

}
