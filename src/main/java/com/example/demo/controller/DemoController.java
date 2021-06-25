package com.example.demo.controller;

import com.example.demo.constant.Status;
import com.example.demo.hander.JsonException;
import com.example.demo.hander.PageException;
import com.example.demo.model.ApiResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class DemoController {
    //访问路径映射  @RequestMapping(value = "hello") 若是使用/表示默认
    @RequestMapping(value = "/hello")
    @ResponseBody
    public String sayHello() {
        return "hello, spring boot";
    }

    @RequestMapping(value = "/instances")
    @ResponseBody
    public String adminIndex() {
        return "这是SpringBoot框架的一个监视器客户端";
    }

    @GetMapping("/json") //映射get方法
    @ResponseBody
    public ApiResponse jsonException(){
        throw new JsonException(Status.UNKNOWN_ERROR);
    }

    @GetMapping("/page")
    public ModelAndView pageException(){
        throw new PageException(Status.UNKNOWN_ERROR);
    }

    @GetMapping(value = "/test")
    @ResponseBody
    public ApiResponse test(){
       throw  new RuntimeException("这是一个运行时错误");
    }
}
