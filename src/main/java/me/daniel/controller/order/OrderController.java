package me.daniel.controller.order;

import me.daniel.domain.DTO.OrderReadyDTO;
import me.daniel.domain.VO.KakaoPayApprovalVO;
import me.daniel.interceptor.auth.Auth;
import me.daniel.responseMessage.Message;
import me.daniel.service.KakaoPayService;
import me.daniel.service.UserService;
import me.daniel.utility.CookieUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Optional;

/**
 * 주문 컨트롤러 <br>
 * 카카오 페이 API 활용하여 주문
 *
 * <pre>
 *     <b>History</b>
 *     김남영, 1.0, 2022.05.27 최초 작성
 * </pre>
 *
 * @author 김남영
 * @version 1.0
 */
@RestController
@RequestMapping("/api/order/pay")
public class OrderController {

    private static final String FAILED_PAY_MESSAGE = "결제 준비 및 조회에 실패했습니다.";
    private static final String FAILED_INFO_MESSAGE = "결제 정보 조회에 실패했습니다.";
    private static final String INVALID_PARAMS = "파라미터를 다시 한번 확인해보세요.";
    private static final String PAY_URI_MSG = "카카오 페이 결제 준비 URI";
    private static final String INFO_URI_MSG = "결제 정보 조회 목록입니다.";
    private static final String EMPTY_CART_DATA = "장바구니 데이터가 없습니다.";

    private static final Logger log = LoggerFactory.getLogger(OrderController.class);

    private final KakaoPayService kakaoPayService;
    private final UserService userService;

    public OrderController(KakaoPayService kakaoPayService, UserService userService) {
        this.kakaoPayService = kakaoPayService;
        this.userService = userService;
    }

    @Auth(role = Auth.Role.BASIC_USER)
    @PostMapping("/ready")
    public Message readyPayAction(@RequestBody OrderReadyDTO orderReadyDTO,
                                  HttpServletRequest request) {

        String uri = kakaoPayService.kakaoPayReady(orderReadyDTO, userService.findById((String) request.getAttribute("tokenUserId")));

        if (uri == null) {
            return new Message
                    .Builder(FAILED_PAY_MESSAGE)
                    .message(INVALID_PARAMS)
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .build();
        }
        return new Message
                .Builder(uri)
                .message(PAY_URI_MSG)
                .httpStatus(HttpStatus.OK)
                .build();
    }

    @PostMapping("cart/ready")
    public Message cartReadyPayAction(HttpServletRequest request) throws UnsupportedEncodingException {
        Cookie[] cookies = request.getCookies();
        Optional<Cookie> cartCookie = CookieUtil.getCartCookie(cookies);

        if (cartCookie.isEmpty()) {
            return new Message
                    .Builder(EMPTY_CART_DATA)
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .build();
        }

        String uri = kakaoPayService.cartKakaoPayReady(CookieUtil.getItemNoArr(cartCookie.get()),
                userService.findById((String) request.getAttribute("tokenUserId")),
                CookieUtil.getTotalAmount(cartCookie.get()));

        if (uri == null) {
            return new Message
                    .Builder(FAILED_PAY_MESSAGE)
                    .message(INVALID_PARAMS)
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .build();
        }
        return new Message
                .Builder(uri)
                .httpStatus(HttpStatus.OK)
                .message(PAY_URI_MSG)
                .build();
    }

    @GetMapping("/completed")
    public Message paySuccessAction(@RequestParam("pg_token") String pg_token, HttpServletRequest request) {

        KakaoPayApprovalVO kakaoPayApprovalVO = kakaoPayService.kakaoPayInfo(pg_token, (String) request.getAttribute("token"));

        if (kakaoPayApprovalVO == null) {
            return new Message
                    .Builder(FAILED_INFO_MESSAGE)
                    .message(INVALID_PARAMS)
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .build();
        }
        return new Message
                .Builder(kakaoPayService.kakaoPayInfo(pg_token, (String) request.getAttribute("token")))
                .message(INFO_URI_MSG)
                .httpStatus(HttpStatus.OK)
                .build();
    }

    @GetMapping("/cancel")
    public ResponseEntity<String> payCancelAction() {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("결제를 취소하셨습니다.");
    }

    @GetMapping("/fail")
    public ResponseEntity<String> payFailAction() {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("결제에 실패했습니다.");
    }
}
