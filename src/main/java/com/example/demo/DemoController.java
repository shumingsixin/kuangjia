package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class DemoController {
    //访问路径映射  @RequestMapping(value = "hello") 若是使用/表示默认
    @RequestMapping(value = "/")
    @ResponseBody
    public String sayHello() {
        return "hello, spring boot";
    }
}
