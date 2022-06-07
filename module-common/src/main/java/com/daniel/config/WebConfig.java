package com.daniel.config;

import com.daniel.interceptor.auth.AuthenticateInterceptor;
import com.daniel.interceptor.auth.AuthorizeInterceptor;
import com.daniel.interceptor.cookie.CookieInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(WebConfig.class);

    private final AuthorizeInterceptor authorizeInterceptor;
    private final AuthenticateInterceptor authenticateInterceptor;
    private final CookieInterceptor cookieInterceptor;

    public WebConfig(AuthorizeInterceptor authorizeInterceptor, AuthenticateInterceptor authenticateInterceptor, CookieInterceptor cookieInterceptor) {
        this.authorizeInterceptor = authorizeInterceptor;
        this.authenticateInterceptor = authenticateInterceptor;
        this.cookieInterceptor = cookieInterceptor;
    }

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
