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
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 인증 인터셉터 <br>
 * 토큰 검증의 인증 처리를 실행한다.
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
public class AuthenticateInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(AuthenticateInterceptor.class);
    private static final String BEARER_TOKEN = "Bearer";
    private static final String NULL_TOKEN = "DB에 토큰이 존재하지 않습니다. 로그인이 필요합니다.";
    private static final String INVALID_TOKEN = "토큰이 일치하지 않습니다. 잘못된 접근입니다.";

    private final AuthorizationExtractor authorizationExtractor;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, Object> redisTemplate;

    public AuthenticateInterceptor(AuthorizationExtractor authorizationExtractor, JwtTokenProvider jwtTokenProvider, RedisTemplate<String, Object> redisTemplate) {
        this.authorizationExtractor = authorizationExtractor;
        this.jwtTokenProvider = jwtTokenProvider;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("인증 처리 인터셉터 실행");

        /* 토큰 추출 및 검증 */
        String requestToken = authorizationExtractor.extract(request, BEARER_TOKEN);
        jwtTokenProvider.validateToken(requestToken);

        /* 토큰 body에 존재하는 아이디와 등급 */
        final String tokenUserId = jwtTokenProvider.getUserId(requestToken);

        /* request에 토큰 유저 권한 추가 */
        request.setAttribute("tokenUserRole", jwtTokenProvider.getUserGrade(requestToken));

        /* Redis DB에 저장된 토큰 추출 */
        final ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        final String redisToken = (String) valueOperations.get(tokenUserId);

        /* DB에 토큰이 존재하지 않을 경우 */
        if (redisToken == null) {
            throw new RedisNullTokenException(NULL_TOKEN);
        }

        /* DB 토큰과 로그인 유저 토큰 정보가 일치하지 않을 경우 */
        if (!redisToken.equals(requestToken)) {
            throw new TokenMismatchException(INVALID_TOKEN);
        }

        return true;
    }
}
