package com.example.demo;

import com.example.demo.properties.MyProperties;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = DemoApplication.class)
public class DemoControllerTest {
    @Test
    public void testSayHello() {
        String result = new DemoController().sayHello();
        System.out.println(result);
    }
    @Autowired
    private MyProperties mp;

    @Test
    public void testMp(){
        System.out.println(mp.getCreator());
    }


}
