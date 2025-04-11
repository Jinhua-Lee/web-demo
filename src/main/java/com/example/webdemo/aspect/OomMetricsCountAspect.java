package com.example.webdemo.aspect;

import com.example.webdemo.mangger.OomMonitor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @author Jinhua-Lee
 */
@Slf4j
@Aspect
@Component
public class OomMetricsCountAspect {

    @Pointcut(
            "execution(@org.springframework.scheduling.annotation.Scheduled * *(..)) || "
            + "execution(@org.springframework.web.bind.annotation.GetMapping * *(..)) || "
            + "execution(@org.springframework.web.bind.annotation.PostMapping * *(..)) || "
            + "execution(@org.springframework.web.bind.annotation.PutMapping * *(..)) || "
            + "execution(@org.springframework.web.bind.annotation.DeleteMapping * *(..))"
    )
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result;
        try {
            result = joinPoint.proceed();
        } catch (OutOfMemoryError oom) {
            log.debug("caught oom error. ", oom);
            OomMonitor.handleOom();
            throw oom;
        }
        return result;
    }
}
