package me.daniel.interceptor;

import me.daniel.exception.TokenEmptyException;
import me.daniel.jwt.AuthorizationExtractor;
import me.daniel.jwt.JwtTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class BearerAuthInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(BearerAuthInterceptor.class);

    private AuthorizationExtractor authorizationExtractor;
    private JwtTokenProvider jwtTokenProvider;

    private static final String INVALID_TOKEN_MESSAGE = "유효하지 않은 토큰입니다.";
    private static final String EMPTY_TOKEN = "토큰이 존재하지 않습니다.";

    public BearerAuthInterceptor(AuthorizationExtractor authorizationExtractor, JwtTokenProvider jwtTokenProvider) {
        this.authorizationExtractor = authorizationExtractor;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = authorizationExtractor.extract(request, "Bearer");
        if (token.isEmpty()) {
            try {
                throw new TokenEmptyException(EMPTY_TOKEN);
            } catch (TokenEmptyException e) {
                e.printStackTrace();
            }
        }

        if (!jwtTokenProvider.validateToken(token)) {
            throw new IllegalArgumentException(INVALID_TOKEN_MESSAGE);
        }

        String id = jwtTokenProvider.getSubject(token);
        request.setAttribute("id", id);
        logger.info("id = " + request.getAttribute("id"));

        return true;
    }
}

