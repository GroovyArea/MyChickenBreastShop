package me.daniel.config;

import me.daniel.interceptor.auth.AuthInterceptor;
import me.daniel.interceptor.cookie.CookieInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(WebConfig.class);

    private final AuthInterceptor authInterceptor;
    private final CookieInterceptor cookieInterceptor;

    public WebConfig(AuthInterceptor authInterceptor, CookieInterceptor cookieInterceptor) {
        this.authInterceptor = authInterceptor;
        this.cookieInterceptor = cookieInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/**");
        registry.addInterceptor(cookieInterceptor)
                .addPathPatterns("/api/carts/**");

    }


}
