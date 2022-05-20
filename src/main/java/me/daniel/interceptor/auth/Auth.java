package me.daniel.interceptor.auth;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value = {ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Auth {

    Role role() default Role.BASIC_USER;

    enum Role {
        BASIC_USER, // 로그인 사용자
        ADMIN, // 관리자
    }
}
