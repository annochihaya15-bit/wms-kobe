package org.example.springboot_wms.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)// 注解只能用在方法上
// 程序运行时才会读取这个注解
@Retention(RetentionPolicy.RUNTIME)
public @interface AdminRequired {
    // 可选参数：是否允许超级管理员访问（默认允许）
    boolean allowSuperAdmin() default true;
}
