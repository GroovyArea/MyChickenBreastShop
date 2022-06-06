package me.daniel.controller.order;

import me.daniel.responseMessage.Message;
import me.daniel.service.KakaoPayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
@RequestMapping("/api/pay")
public class PayController {

    private static final String CANCEL_PAY = "결제가 취소되었습니다.";

    private static final Logger log = LoggerFactory.getLogger(PayController.class);

    private final KakaoPayService kakaoPayService;

    public PayController(KakaoPayService kakaoPayService) {
        this.kakaoPayService = kakaoPayService;
    }

    @PostMapping("/cancel")
    public Message payCancel(@RequestBody Map<String, Object> map){
        return new Message
                .Builder(kakaoPayService.cancelKakaoPay(map))
                .mediaType(MediaType.APPLICATION_JSON)
                .httpStatus(HttpStatus.OK)
                .message(CANCEL_PAY)
                .build();
    }
}
