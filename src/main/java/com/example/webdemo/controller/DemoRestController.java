package com.example.webdemo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Todo
 *
 * @author Jinhua-Lee
 * @version 1.0
 * @date 2022/2/11 22:47
 */
@RestController
@RequestMapping(value = "demo-rest")
public class DemoRestController {

    @GetMapping(value = "hello")
    public String hello() {
        return "hello";
    }
}
