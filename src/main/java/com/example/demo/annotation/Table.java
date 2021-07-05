package com.example.demo.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 表注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE) //目标适用类、接口（包括注解类型）或枚举
public @interface Table {
    String name();
}
