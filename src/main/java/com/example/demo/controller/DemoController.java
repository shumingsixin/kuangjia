package com.example.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {
    //访问路径映射  @RequestMapping(value = "hello") 若是使用/表示默认
    @RequestMapping(value = "/hello")
    public String sayHello() {
        return "hello, spring boot";
    }

    @RequestMapping(value = "/instances")
    public String adminIndex() {
        return "这是SpringBoot框架的一个监视器客户端";
    }
}
