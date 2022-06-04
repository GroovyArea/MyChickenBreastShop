package me.daniel.controller.order;

import me.daniel.domain.DTO.order.PayApprovalDTO;
import me.daniel.exceptions.RunOutOfStockException;
import me.daniel.interceptor.auth.Auth;
import me.daniel.responseMessage.Message;
import me.daniel.service.KakaoPayService;
import me.daniel.service.OrderService;
import me.daniel.utility.CookieUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Map;
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
@RequestMapping("/api/order")
public class OrderController {

    private static final String FAILED_PAY_MESSAGE = "결제 준비 및 조회에 실패했습니다.";
    private static final String FAILED_INFO_MESSAGE = "결제 정보 조회에 실패했습니다.";
    private static final String INVALID_PARAMS = "파라미터를 다시 한번 확인해보세요.";
    private static final String PAY_URI_MSG = "카카오 페이 결제 URL";
    private static final String INFO_URI_MSG = "결제 정보 조회 목록입니다.";
    private static final String EMPTY_CART_DATA = "장바구니 데이터가 없습니다.";
    private static final String MEMBER_ORDER_LIST = "회원 주문 조회 리스트입니다.";
    private static final String ORDER_INFO = "주문 상세 조회입니다.";
    private static final String CANCELED_PAY_MESSAGE = "결제를 취소하셨습니다.";

    private static final Logger log = LoggerFactory.getLogger(OrderController.class);

    private final KakaoPayService kakaoPayService;
    private final OrderService orderService;

    public OrderController(KakaoPayService kakaoPayService, OrderService orderService) {
        this.kakaoPayService = kakaoPayService;
        this.orderService = orderService;
    }

    @GetMapping("/{userId}")
    public Message getDBOrderInfo(@PathVariable String userId) {
        return new Message
                .Builder(orderService.getOrderInfoList(userId))
                .message(MEMBER_ORDER_LIST)
                .httpStatus(HttpStatus.OK)
                .mediaType(MediaType.APPLICATION_JSON)
                .build();
    }

    @GetMapping("/detail")
    public Message getOrderDetail(@RequestParam String tid, @RequestParam String cid) {
        return new Message
                .Builder(kakaoPayService.getOrderDetail(tid, cid))
                .message(ORDER_INFO)
                .httpStatus(HttpStatus.OK)
                .mediaType(MediaType.APPLICATION_JSON)
                .build();
    }

    @Auth(role = Auth.Role.BASIC_USER)
    @PostMapping
    public Message orderAction(@RequestBody Map<String, Object> map,
                               HttpServletRequest request) throws RunOutOfStockException {

        String url = kakaoPayService.getkakaoPayUrl(map, request);

        if (url == null) {
            return new Message
                    .Builder(FAILED_PAY_MESSAGE)
                    .message(INVALID_PARAMS)
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .build();
        }

        return new Message
                .Builder(url)
                .message(PAY_URI_MSG)
                .httpStatus(HttpStatus.OK)
                .build();
    }

    @Auth(role = Auth.Role.BASIC_USER)
    @PostMapping("/cart")
    public Message cartOrderAction(HttpServletRequest request) throws UnsupportedEncodingException, RunOutOfStockException {
        Cookie[] cookies = request.getCookies();
        Optional<Cookie> cartCookie = CookieUtil.getCartCookie(cookies);

        if (cartCookie.isEmpty()) {
            return new Message
                    .Builder(EMPTY_CART_DATA)
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .build();
        }

        String url = kakaoPayService.getCartKakaoPayUrl(CookieUtil.getItemNoArr(cartCookie.get()),
                CookieUtil.getStockArr(cartCookie.get()),
                CookieUtil.getTotalAmount(cartCookie.get()), request);

        if (url == null) {
            return getFailedPayMessage();
        }

        return new Message
                .Builder(url)
                .httpStatus(HttpStatus.OK)
                .message(PAY_URI_MSG)
                .build();
    }


    @GetMapping("/completed")
    public Message paySuccessAction(@RequestParam("pg_token") String pg_token) {

        PayApprovalDTO kakaoPayReadyDTO = kakaoPayService.getKakaoPayInfo(pg_token);

        if (kakaoPayReadyDTO == null) {
            return new Message
                    .Builder(FAILED_INFO_MESSAGE)
                    .message(INVALID_PARAMS)
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .build();
        }
        return new Message
                .Builder(kakaoPayReadyDTO)
                .message(INFO_URI_MSG)
                .httpStatus(HttpStatus.OK)
                .build();
    }

    @GetMapping("/cancel")
    public ResponseEntity<String> payCancelAction() {
        return ResponseEntity.ok().body(CANCELED_PAY_MESSAGE);
    }

    @GetMapping("/fail")
    public ResponseEntity<String> payFailAction() {
        return ResponseEntity.ok().body(FAILED_PAY_MESSAGE);
    }

    private Message getFailedPayMessage() {
        return new Message
                .Builder(FAILED_PAY_MESSAGE)
                .message(INVALID_PARAMS)
                .httpStatus(HttpStatus.BAD_REQUEST)
                .build();
    }
}
