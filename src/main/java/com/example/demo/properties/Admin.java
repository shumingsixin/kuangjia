package com.example.demo.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.ImportResource;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @PropertySource(value ={"classpath:文件名}) 加载指定配置文件
 * @ConfigurationProperties 告诉SpringBoot将本类中的所有属性和配置文件相关的配置进行绑定;prefix="admin":映射配置文件那个的属性
 * @Value 需要一个个指定 ConfigurationPreperties批量映射
 *
 * @ImportResource(locations = {"classpath:remark.xml"}) 导入Spring的配置文件让其生效
 */

@Data//直接get set方法
@Component //开启自动配置
@ConfigurationProperties(prefix = "admin")
public class Admin {

    //@Value("${com.example.demo.username}")
    private String username;

    //@Value("${com.example.demo.pwd}")
    private String pwd;

    private List<Object> list;

}
