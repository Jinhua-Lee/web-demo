package com.example.webdemo.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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

    @HystrixCommand(
            fallbackMethod = "hangupFallback",
            threadPoolKey = "hangup",
            commandKey = "hangup"
    )
    @GetMapping(value = "/hangup")
    public String hangup(@RequestParam("hangUpSeconds") Integer hangUpSeconds) throws InterruptedException {
        log.info("start to hang up.");
        TimeUnit.SECONDS.sleep(hangUpSeconds);
        log.info("hang up finished for {} seconds.", hangUpSeconds);
        return "hangup";
    }

    @HystrixCommand(
            fallbackMethod = "hangupFallback2",
            threadPoolKey = "hangup2",
            commandKey = "hangup2"
    )
    @GetMapping(value = "/hangup2")
    public String hangup2(@RequestParam("hangUpSeconds") Integer hangUpSeconds) throws InterruptedException {
        log.info("start to hang up 2.");
        TimeUnit.SECONDS.sleep(hangUpSeconds);
        log.info("hang up 2 finished for {} seconds.", hangUpSeconds);
        return "hangup2";
    }

    private String hangupFallback(Integer hangUpSeconds) {
        log.info("hang up fallback method.");
        return "hangup fallback";
    }

    private String hangupFallback2(Integer hangUpSeconds) {
        log.info("hang up fallback method 2.");
        return "hangup fallback2";
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

    @GetMapping(value = "/memory-alloc")
    public Integer memoryAlloc(@RequestParam(value = "allocMegaBytes") Integer allocMegaBytes) {
        allocMegaBytes = allocMegaBytes <= 0 ? 100 : allocMegaBytes;
        log.info("start to memory alloc. total memory = {} mb.", allocMegaBytes);
        try {
            byte[] curBytes = new byte[allocMegaBytes * 1024 * 1024];
            log.info("allocate {} bytes", curBytes.length);
        } catch (OutOfMemoryError oom) {
            log.error("memory alloc error.", oom);
            throw oom;
        }
        return allocMegaBytes;
    }

    @GetMapping(value = "/thread-oom")
    @SuppressWarnings("all")
    public Integer threadOom() {
        while (true) {
            log.info("[thread oom] thread count = {}", threadCount.getAndIncrement());
            new Thread(() -> {
                try {
                    TimeUnit.SECONDS.sleep(1_000);
                } catch (InterruptedException ignored) {
                }
            }).start();
        }
    }

    @PostMapping(value = "/exit")
    @SuppressWarnings("all")
    public Integer restart() {
        // alertManager要求必须Post请求
        new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException ignored) {
            }
            System.exit(0);
        }).start();
        return 0;
    }
}
