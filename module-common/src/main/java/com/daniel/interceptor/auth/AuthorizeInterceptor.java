package com.daniel.interceptor.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.security.sasl.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.daniel.interceptor.auth.Auth.Role.ADMIN;

/**
 * 권한 처리 인터셉터 <br>
 * 에너테이션 권한 처리를 실행한다.
 *
 * <pre>
 *     <b>History</b>
 *     김남영, 1.0, 2022.05.20 최초 작성
 *     김남영, 1.1, 2022.05.24 인증, 인가 분리
 * </pre>
 *
 * @author 김남영
 * @version 1.1
 */
@Component
public class AuthorizeInterceptor implements HandlerInterceptor {

    private static final String NOT_ADMIN_AUTH = "관리자 권한을 필요로 하는 접근입니다.";

    private static final Logger log = LoggerFactory.getLogger(AuthorizeInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("권한 처리 인터셉터 실행");

        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        /* 핸들러메서드 에너테이션 값 추출 */
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Auth auth = handlerMethod.getMethodAnnotation(Auth.class);

        /* 권한이 필요 없는 접근 */
        if (auth == null) {
            return true;
        }

        /* 에너테이션 값 => 관리자일 경우 */
        if (auth.role() == ADMIN) {
            /* 로그인 유저 권한이 관리자가 아닐 경우 */
            if (!request.getAttribute("tokenUserRole").toString().equals(ADMIN.toString())) {
                throw new AuthenticationException(NOT_ADMIN_AUTH);
            }
        }

        return true;
    }


}
