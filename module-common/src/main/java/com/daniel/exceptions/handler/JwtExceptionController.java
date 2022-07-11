package com.daniel.exceptions.handler;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import com.daniel.exceptions.error.TokenEmptyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Jwt 토큰 예외 처리 <br>
 * 토큰 관련 예외 발생 시 메시지 응답
 *
 * @author 김남영
 */
@RestControllerAdvice
public class JwtExceptionController {

    private static final String EMPTY_TOKEN = "헤더에 저장된 토큰이 필요합니다.";
    private static final String INVALID_SIGNATURE = "토큰 형식이 잘못 되었습니다.";
    private static final String CLASS_CAST_FAIL = "토큰 유효성 검사가 실패하였습니다. 확인 후 재요청 바랍니다.";
    private static final String MALFORMED_TOKEN = "유효하지 않은 토큰입니다.";
    private static final String EXPIRED_TOKEN = "토큰 유효기간이 만료되었습니다. 재로그인 바랍니다.";
    private static final String UNSUPPORTED_TOKEN = "지원하지 않는 토큰입니다.";

    @ExceptionHandler(TokenEmptyException.class)
    public ResponseEntity<String> tokenEmptyException() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(EMPTY_TOKEN);
    }

    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<String> signatureException() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(INVALID_SIGNATURE);
    }

    @ExceptionHandler(ClassCastException.class)
    public ResponseEntity<String> classCastException() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(CLASS_CAST_FAIL);
    }

    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<String> malformedJwtException() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(MALFORMED_TOKEN);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<String> expiredJwtException() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(EXPIRED_TOKEN);
    }

    @ExceptionHandler(UnsupportedJwtException.class)
    public ResponseEntity<String> unsupportedJwtException() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(UNSUPPORTED_TOKEN);
    }

}
