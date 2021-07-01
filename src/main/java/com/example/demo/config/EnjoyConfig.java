package com.example.demo.config;

import com.jfinal.template.ext.spring.JFinalViewResolver;
import com.jfinal.template.source.ClassPathSourceFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 模板配置类
 */
@Configuration
public class EnjoyConfig {

    @Bean(name = "jfinalViewResolver")
    public JFinalViewResolver getJFinalViewResolver() {
        JFinalViewResolver jfr = new JFinalViewResolver();
        jfr.setDevMode(true); //放在最前面
        //使用类工厂从路径和jar包中加载模板文件
        jfr.setSourceFactory(new ClassPathSourceFactory());
        //设置模板路径 下面也可以代替这句
        jfr.setPrefix("/templates/page/");
        //JFinalViewResolver.engine.setBaseTemplatePath("/templates/");
        jfr.setSessionInView(true);//页面可以获取
        jfr.setSuffix(".html");//设置后缀
        jfr.setContentType("text/html;charset=UTF-8");
        jfr.setOrder(0);
        return jfr;

    }
}
