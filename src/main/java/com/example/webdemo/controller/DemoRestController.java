package com.example.webdemo.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.web.bind.annotation.*;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
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

    @SuppressWarnings("all")
    private final List<byte[]> dataList = new ArrayList<>();
    private Integer totalMegaBytes = 0;

    @SuppressWarnings("all")
    private List<Class<?>> classes = new ArrayList<>();

    @SuppressWarnings("all")
    private List<ByteBuffer> buffers = new ArrayList<>();

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

    @SuppressWarnings(value = "unused")
    private String hangupFallback(Integer hangUpSeconds) {
        log.info("hang up fallback method.");
        return "hangup fallback";
    }

    @SuppressWarnings(value = "unused")
    private String hangupFallback2(Integer hangUpSeconds) {
        log.info("hang up fallback method 2.");
        return "hangup fallback2";
    }

    @GetMapping(value = "/memory-out")
    public Integer memoryOut(@RequestParam(value = "addMegaBytes", required = false) Integer addMegaBytes) {
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
    public Integer memoryAlloc(@RequestParam(value = "allocMegaBytes", required = false) Integer allocMegaBytes) {
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

    @SuppressWarnings("all")
    @GetMapping(value = "gc-overhead")
    public Integer gcOverheadLimit() {
        List<List<byte[]>> bytes = new ArrayList<>();
        while (true) {
            List<byte[]> curBytes = new ArrayList<>();
            for (int i = 0; i < 100_000; i++) {
                curBytes.add(new byte[1 * 32]);
            }
            bytes.add(curBytes.subList(0, 10_000));
        }
    }


    @Data
    @NoArgsConstructor
    private static class ClassHolder {
    }

    @GetMapping(value = "/meta-oom")
    @SuppressWarnings(value = "all")
    public Integer metaOom() {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(ClassHolder.class);
        // 禁用缓存，强制生成新类
        enhancer.setUseCache(false);
        enhancer.setCallback((MethodInterceptor) (obj, method, args1, proxy) -> proxy.invokeSuper(obj, args1));

        while (true) {
            try {
                // 生成新类并保留引用
                Class<?> clazz = enhancer.create().getClass();
                classes.add(clazz);
            } catch (OutOfMemoryError oom) {
                log.error("meta oom error.", oom);
                break;
            }
        }
        return classes.size();
    }

    @GetMapping(value = "/direct-oom")
    public Integer directOom() {
        // 用ByteBuffer模拟直接内存溢出
        int allocated = 0;
        while (true) {
            try {
                buffers.add(
                        ByteBuffer.allocateDirect(1024 * 1024)
                );
                allocated += 1;
                log.info("allocated {} mb.", allocated);
            } catch (OutOfMemoryError oom) {
                log.error("direct oom error.", oom);
                break;
            }
        }
        return allocated;
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
