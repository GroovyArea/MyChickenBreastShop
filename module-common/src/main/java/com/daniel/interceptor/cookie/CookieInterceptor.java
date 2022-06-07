package com.daniel.interceptor.cookie;

import com.daniel.exceptions.EmptyCookiesException;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 쿠키 인터셉터
 * 쿠키가 들어 있는지 검사한다.
 *
 * <pre>
 *     <b>History</b>
 *     김남영, 1.0, 2022.05.24 최초 작성
 * </pre>
 *
 * @author 김남영
 * @version 1.0
 */

@Component
public class CookieInterceptor implements HandlerInterceptor {

    private static final String NULL_COOKIE = "쿠키가 없습니다.";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        if (request.getCookies() == null) {
            throw new EmptyCookiesException(NULL_COOKIE);
        }
        return true;
    }
}
