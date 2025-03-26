package com.example.webdemo.aspect;

import com.example.webdemo.mangger.OomMonitor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * @author Jinhua-Lee
 */
@Aspect
public class OomMetricsCountAspect {

    @Pointcut("execution(public * com.example.webdemo.controller.*.*(..))")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result;
        try {
            result = joinPoint.proceed();
        } catch (OutOfMemoryError oom) {
            OomMonitor.handleOom();
            throw oom;
        }
        return result;
    }
}
