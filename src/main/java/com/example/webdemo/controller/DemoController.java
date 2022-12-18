package com.example.webdemo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Model和View的Controller
 *
 * @author Jinhua-Lee
 * @version 1.0
 * @date 2022/12/17 19:49
 */
@Controller
@RequestMapping(value = "/demo")
public class DemoController {

    @GetMapping(value = "/hello")
    public String hello() {
        return "/templates/main.html";
    }
}
