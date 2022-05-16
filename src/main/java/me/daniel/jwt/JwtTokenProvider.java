package me.daniel.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;

/**
 * 토큰 제공 클래스
 * 토큰 생성, 유효성 검사, 값 추출
 */
@Component
public class JwtTokenProvider {

    private final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    private static final String SECRET_KEY = "mysecretmysecretmysecretmysecretmysecretasdfasdfasdf";
    private static final long VALIDATE_IN_MILLISECONDS = 1000 * 60L * 1L;

    /**
     * 토큰 생성
     *
     * @param id 유저 아이디
     * @return 토큰 값
     */
    public String createToken(String id) {
        Claims claims = Jwts.claims().setSubject(String.valueOf(id));

        Date now = new Date();
        Date validity = new Date(now.getTime() + VALIDATE_IN_MILLISECONDS);
        logger.info("now: {}", now);
        logger.info("validity: {}", validity);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, Base64.getEncoder().encodeToString(SECRET_KEY.getBytes()))
                .compact();
    }

    /**
     * 토큰 값 추출
     *
     * @param token 토큰
     * @return 토큰 값
     */
    public String getSubject(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY.getBytes())
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * 토큰 유효성 검사
     *
     * @param token 토큰
     * @return 논리 값
     */
    public boolean validateToken(String token) {
        Jws<Claims> claims = Jwts
                .parser()
                .setSigningKey(SECRET_KEY.getBytes())
                .parseClaimsJws(token);
        return !claims.getBody().getExpiration().before(new Date());
    }

}
