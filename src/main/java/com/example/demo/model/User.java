package com.example.demo.model;

import com.example.demo.annotation.Column;
import com.example.demo.annotation.Pk;
import com.example.demo.annotation.Table;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Table(name = "user")
@Data
public class User implements Serializable {
    @Pk
    private Long id;
    private String name;
    private String password;

    @Column(name = "birth_date")
   // @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) //可用全局搭配配置
    //@JsonFormat(pattern = "yyyy-MM-dd")//设定全局日期格式之后 传出设置单独处理的
    private Date birthDate;
    /**
     * 加密的盐
     */
    private String salt;

    @Column(name = "phone_number")
    private String phoneNumber;
    /**
     * 状态 -1:逻辑删除, 0:禁用,1:启用
     * 使用泛型 防止默认值为0的情况
     */
    private Integer status;


    @Column(name = "create_time")
    private Date createTime;


    @Column(name = "last_login_time")
    private Date lastLoginTime;


    @Column(name = "last_update_time")
    private Date lastUpdateTime;
}
