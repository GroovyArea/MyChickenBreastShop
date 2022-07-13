package com.daniel.filter;

import com.daniel.exceptions.error.RedisNullTokenException;
import com.daniel.exceptions.error.TokenMismatchException;
import com.daniel.jwt.AuthorizationExtractor;
import com.daniel.jwt.JwtTokenProvider;
import com.daniel.service.RedisService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private static final String BEARER_TOKEN = "Bearer";
    private static final String NULL_TOKEN = "DB에 토큰이 존재하지 않습니다. 로그인이 필요합니다.";
    private static final String INVALID_TOKEN = "토큰이 일치하지 않습니다. 잘못된 접근입니다.";
    private static final String INVALID_SIGNATURE = "토큰 형식이 잘못 되었습니다.";
    private static final String CLASS_CAST_FAIL = "토큰 유효성 검사가 실패하였습니다. 확인 후 재요청 바랍니다.";
    private static final String MALFORMED_TOKEN = "유효하지 않은 토큰입니다.";
    private static final String EXPIRED_TOKEN = "토큰 유효기간이 만료되었습니다. 재로그인 바랍니다.";
    private static final String UNSUPPORTED_TOKEN = "지원하지 않는 토큰입니다.";


    private final AuthorizationExtractor authorizationExtractor;

    private final JwtTokenProvider jwtTokenProvider;

    private final RedisService redisService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();

        if (!requestURI.startsWith("/api")) {
            filterChain.doFilter(request, response);
        }

        try {
            String requestToken = authorizationExtractor.extract(request, BEARER_TOKEN);
            jwtTokenProvider.validateToken(requestToken);

            final String tokenUserId = jwtTokenProvider.getUserId(requestToken);

            /* request에 토큰 유저 권한 및 아이디 추가 */
            request.setAttribute("tokenUserRole", jwtTokenProvider.getUserGrade(requestToken));
            request.setAttribute("tokenUserId", jwtTokenProvider.getUserId(requestToken));
            request.setAttribute("token", requestToken);

            /* Redis DB에 저장된 토큰 추출 */
            final String redisToken = redisService.getData(tokenUserId);

            /* DB에 토큰이 존재하지 않을 경우 */
            if (redisToken == null) {
                throw new RedisNullTokenException(NULL_TOKEN);
            }

            /* DB 토큰과 로그인 유저 토큰 정보가 일치하지 않을 경우 */
            if (!redisToken.equals(requestToken)) {
                throw new TokenMismatchException(INVALID_TOKEN);
            }

            filterChain.doFilter(request, response);

        } catch (SignatureException e) {
            setErrorResponse(response, INVALID_SIGNATURE, e);
        } catch (RedisNullTokenException e) {
            setErrorResponse(response, NULL_TOKEN, e);
        } catch (TokenMismatchException e) {
            setErrorResponse(response, INVALID_TOKEN, e);
        } catch (ClassCastException e) {
            setErrorResponse(response, CLASS_CAST_FAIL, e);
        } catch (MalformedJwtException e) {
            setErrorResponse(response, MALFORMED_TOKEN, e);
        } catch (ExpiredJwtException e) {
            setErrorResponse(response, EXPIRED_TOKEN, e);
        } catch (UnsupportedJwtException e) {
            setErrorResponse(response, UNSUPPORTED_TOKEN, e);
        }
    }

    private void setErrorResponse(HttpServletResponse response, String message, Exception e) throws IOException {
        log.error(e.getMessage());
        response.setCharacterEncoding("utf-8");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.getWriter().write(message);
    }
}
