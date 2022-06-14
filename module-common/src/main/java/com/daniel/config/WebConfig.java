package com.daniel.config;

import com.daniel.interceptor.auth.AuthenticateInterceptor;
import com.daniel.interceptor.auth.AuthorizeInterceptor;
import com.daniel.interceptor.cookie.CookieInterceptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class WebConfig implements WebMvcConfigurer {

    private final AuthorizeInterceptor authorizeInterceptor;
    private final AuthenticateInterceptor authenticateInterceptor;
    private final CookieInterceptor cookieInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authenticateInterceptor)
                .addPathPatterns("/api/**")
                .excludePathPatterns("/user");
        registry.addInterceptor(authorizeInterceptor)
                .addPathPatterns("/api/**")
                .excludePathPatterns("/user");
        registry.addInterceptor(cookieInterceptor)
                .addPathPatterns("/api/carts/**");

    }


}
