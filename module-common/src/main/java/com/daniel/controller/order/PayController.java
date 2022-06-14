package com.daniel.controller.order;

import com.daniel.response.Message;
import com.daniel.service.KakaoPayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 결제 컨트롤러 <br>
 * 카카오 페이 API 활용
 *
 * <pre>
 *     <b>History</b>
 *     김남영, 1.0, 2022.06.03 최초 작성
 * </pre>
 *
 * @author 김남영
 * @version 1.0
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/pay")
public class PayController {

    private static final String CANCEL_PAY = "결제가 취소되었습니다.";

    private final KakaoPayService kakaoPayService;

    @PostMapping("/cancel")
    public ResponseEntity<Message> payCancel(@RequestBody Map<String, Object> map) {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(
                Message.builder()
                        .data(kakaoPayService.cancelKakaoPay(map))
                        .message(CANCEL_PAY)
                        .build()
        );
    }
}
