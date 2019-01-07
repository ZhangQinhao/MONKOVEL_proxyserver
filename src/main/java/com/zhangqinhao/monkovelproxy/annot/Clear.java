package com.zhangqinhao.monkovelproxy.annot;

import org.springframework.web.servlet.HandlerInterceptor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Clear {
    Class<? extends HandlerInterceptor>[] value() default {};
}
