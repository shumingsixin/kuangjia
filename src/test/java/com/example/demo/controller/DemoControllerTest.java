package com.example.demo.controller;

import com.example.demo.properties.Admin;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class DemoControllerTest {
    @Test
    public void testSayHello() {
        String result = new DemoController().sayHello();
        System.out.println(result);
    }

    @Autowired
    private Admin mp;

    @Test
    public void testMp() {
        System.out.println("配置文件的映射:" + this.mp.getUsername());
    }


    @Test
    public void log() {
        Logger log = LoggerFactory.getLogger(Admin.class);
        log.info("这是一个用来测试配置文件的映射的类 ");
    }


}
