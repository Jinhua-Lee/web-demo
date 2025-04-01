package com.example.webdemo.controller.fallback;

import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;

/**
 * @author Jinhua-Lee
 */
@DefaultProperties(defaultFallback = "globalFallback")
public class DefaultFallback {
}
