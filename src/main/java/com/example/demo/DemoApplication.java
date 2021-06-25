package com.example.demo;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @SpringBootApplicationg 用来标注一个主程序类, 说明是一个Spring Boot应用
 * @SpringBootConfiguration 用来标注SpringBoot的配置类
 * @Coinfiguration 配置类上标注这个注解
 * @Component,@EnableAutoConfiguration 开启自动配置功能
 * @AutoConfigurationPackage 自动配置包
 * @Import(AutoConfigurationPackages.Registrar.class) 给容器导入一个组件
 */
@SpringBootApplication
@Slf4j//引入日志工具
//@EnableAdminServer //监控程序的服务入口
public class DemoApplication {

    public static void main(String[] args) {
        //Spring应用启动起来
        SpringApplication.run(DemoApplication.class, args);
    }

    public static void testLog() {
      /*  ConfigurableApplicationContext context = SpringApplication.run(DemoApplication.class, args);
        int length = context.getBeanDefinitionNames().length;
        log.trace("SpringBoot 启动类初始化了{}个Bean", length);
        log.debug("SpringBoot 启动类初始化了{}个Bean", length);
        log.info("SpringBoot 启动类初始化了{}个Bean", length);
        log.warn("SpringBoot 启动类初始化了{}个Bean", length);
        log.error("SpringBoot 启动类初始化了{}个Bean", length);
        try {
            int i = 0;
            int j = 1 / i;
        } catch (Exception e) {
            log.error("SpringBootApplication启动异常:", e);
        }*/
    }

}
