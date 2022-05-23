package me.daniel.interceptor.auth;

import me.daniel.exceptions.RedisNullTokenException;
import me.daniel.exceptions.TokenMismatchException;
import me.daniel.jwt.AuthorizationExtractor;
import me.daniel.jwt.JwtTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.security.sasl.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static me.daniel.interceptor.auth.Auth.Role.ADMIN;

/**
 * 권한 처리 인터셉터 <br>
 * 토큰 검증 및 에너테이션 권한 처리를 실행한다.
 *
 * <pre>
 *     <b>History</b>
 *     김남영, 1.0, 2022.05.20 최초 작성
 * </pre>
 *
 * @author 김남영
 * @version 1.0
 */
@Component
public class AuthInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(AuthInterceptor.class);
    private static final String BEARER_TOKEN = "Bearer";

    private final AuthorizationExtractor authorizationExtractor;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, String> redisTemplate;

    public AuthInterceptor(AuthorizationExtractor authorizationExtractor, JwtTokenProvider jwtTokenProvider, RedisTemplate<String, String> redisTemplate) {
        this.authorizationExtractor = authorizationExtractor;
        this.jwtTokenProvider = jwtTokenProvider;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        /* 핸들러메서드 에너테이션 값 추출 */
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Auth auth = handlerMethod.getMethodAnnotation(Auth.class);

        if (auth == null) {
            return true;
        }

        /* 토큰 추출 및 검증 */
        String requestToken = authorizationExtractor.extract(request, BEARER_TOKEN);
        jwtTokenProvider.validateToken(requestToken);

        /* 토큰 body에 존재하는 아이디와 등급 */
        final String tokenUserId = jwtTokenProvider.getUserId(requestToken);
        final String tokenUserRole = jwtTokenProvider.getUserGrade(requestToken);

        /* Redis DB에 저장된 토큰 추출 */
        final ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        final String redisToken = valueOperations.get(tokenUserId);

        /* DB에 토큰이 존재하지 않을 경우 */
        if (redisToken == null) {
            throw new RedisNullTokenException(AuthMessages.NULL_TOKEN.getMessage());
        }

        /* DB 토큰과 로그인 유저 토큰 정보가 일치하지 않을 경우 */
        if (!redisToken.equals(requestToken)) {
            throw new TokenMismatchException(AuthMessages.INVALID_TOKEN.getMessage());
        }

        /* 에너테이션 값 => 관리자일 경우 */
        if (auth.role() == ADMIN) {
            /* 로그인 유저 권한이 관리자가 아닐 경우 */
            if (!tokenUserRole.equals(ADMIN.toString())) {
                throw new AuthenticationException(AuthMessages.NOT_ADMIN_AUTH.getMessage());
            }
        }
        return true;
    }
}
