package com.daniel.controller.user;

import com.daniel.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class LogoutController {

    private static final String LOGOUT_MESSAGE = "Logout succeed";
    private static final String LOGOUT_FAILED = "로그아웃에 실패했습니다. 토큰을 확인하세요.";

    private final RedisService redisService;

    /**
     * logout 처리
     *
     * @param userId 로그아웃 DTO
     * @return Message 응답 정보 객체
     */
    @GetMapping("/user/logout/{userId}")
    public ResponseEntity<String> logoutAction(@PathVariable String userId) {
        if (redisService.deleteData(userId)) {
            return ResponseEntity
                    .ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(LOGOUT_MESSAGE);
        }
        return ResponseEntity
                .badRequest()
                .body(LOGOUT_FAILED);
    }
}
