package com.daniel.controller.user;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class LogoutController {

    private static final String LOGOUT_MESSAGE = "Logout succeed";

    private final RedisTemplate<String, String> redisTemplate;

    public LogoutController(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * logout 처리
     *
     * @param userId 로그아웃 DTO
     * @return Message 응답 정보 객체
     */
    @GetMapping("/user/logout/{userId}")
    public ResponseEntity<String> logoutAction(@PathVariable String userId) {
        redisTemplate.delete(userId);
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(LOGOUT_MESSAGE);
    }
}
