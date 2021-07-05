package com.example.demo.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 列注解
 */
@Retention(RetentionPolicy.RUNTIME) //滞留方针-运行
@Target({ElementType.FIELD}) //目标是元素类型中的字段 适用适用field属性，也包括enum常量
public @interface Column {
    String name();
}
