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
 * 토큰 제공 클래스 <br>
 * 토큰 생성, 유효성 검사, 값 추출
 *
 * @author 김남영
 * @version 1.1
 */
@Component
public class JwtTokenProvider {

    private final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    private static final String SECRET_KEY = "mysecretmysecretmysecretmysecretmysecretasdfasdfasdf";
    private static final long VALIDATE_IN_MILLISECONDS = 1000 * 60L * 30L;

    /**
     * 토큰 생성
     *
     * @param id 유저 아이디
     * @return 토큰 값
     */
    public String createToken(String id, String grade) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + VALIDATE_IN_MILLISECONDS);
        logger.info("now: {}", now);
        logger.info("validity: {}", validity);
        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject("Login Token")
                .claim("userId", id)
                .claim("userGrade", grade)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, Base64.getEncoder().encodeToString(SECRET_KEY.getBytes()))
                .compact();
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

    /**
     * 토큰에서 아이디 값 추출
     *
     * @param token 토큰
     * @return 로그인 유저 아이디
     */
    public String getUserId(String token) {
        return (String) Jwts.parser()
                .setSigningKey(SECRET_KEY.getBytes())
                .parseClaimsJws(token)
                .getBody()
                .get("userId");
    }

    /**
     * 토큰에서 유저 등급 추출
     *
     * @param token 토큰
     * @return 로그인 유저 등급
     */
    public String getUserGrade(String token) {
        return (String) Jwts.parser()
                .setSigningKey(SECRET_KEY.getBytes())
                .parseClaimsJws(token)
                .getBody()
                .get("userGrade");
    }

    /**
     * 토큰 만료 시간 추출
     *
     * @param token 토큰
     * @return 토큰 만료 시간
     */
    public Date getExpireDate(String token) {
        return (Date) Jwts.parser()
                .setSigningKey(SECRET_KEY.getBytes())
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
    }

}
