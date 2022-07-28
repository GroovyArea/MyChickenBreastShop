package com.daniel.config;

import com.daniel.interceptor.auth.AuthorizeInterceptor;
import com.daniel.interceptor.cookie.CookieInterceptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class WebConfig implements WebMvcConfigurer {

    @Value("${file.upload.location}")
    private String filePath;

    private final AuthorizeInterceptor authorizeInterceptor;
    private final CookieInterceptor cookieInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authorizeInterceptor)
                .addPathPatterns("/api/**")
                .excludePathPatterns("/user");
        registry.addInterceptor(cookieInterceptor)
                .addPathPatterns("/api/carts/**");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        WebMvcConfigurer.super.addResourceHandlers(registry);

        registry.addResourceHandler("/chickenBreastImages/**").addResourceLocations("file:///" + filePath)
                .setCachePeriod(3600)
                .resourceChain(true)
                .addResolver(new PathResourceResolver());
    }
}
