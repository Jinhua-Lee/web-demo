package com.example.webdemo.mangger;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Metrics;

/**
 * @author Jinhua-Lee
 */
public class OomMonitor {
    private static final Counter OOM_COUNTER = Metrics.counter("jvm.oom.errors");

    public static void handleOom() {
        OOM_COUNTER.increment();
    }
}