package com.daniel.controller.order;

import com.daniel.domain.dto.order.request.OrderProductDTO;
import com.daniel.domain.dto.order.response.PayApprovalDTO;
import com.daniel.exceptions.error.RunOutOfStockException;
import com.daniel.interceptor.auth.Auth;
import com.daniel.response.Message;
import com.daniel.service.CartService;
import com.daniel.service.KakaoPayService;
import com.daniel.service.OrderService;
import com.daniel.utility.CookieUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

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
@RequiredArgsConstructor
@Slf4j
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

    private final KakaoPayService kakaoPayService; //TODO OrderController인데 KakaoPayService 의존성이 필요없도록 설계 고민해보면 좋을거같아요
    private final OrderService orderService;
    private final CartService cartService;

    @GetMapping("/{userId}")
    @Auth(role = Auth.Role.BASIC_USER)
    public ResponseEntity<Message> getDBOrderInfo(@PathVariable String userId) {
        //TODO contentType을 명시해주는 이유가 있나요?
        // 그리고 ResponseEntity를 여기서 사용하는 이유가 있나요??
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(
                Message.builder()
                        .data(orderService.getOrderInfoList(userId))
                        .message(MEMBER_ORDER_LIST)
                        .build());
    }

    @GetMapping("/detail")
    @Auth(role = Auth.Role.BASIC_USER)
    public ResponseEntity<Message> getOrderDetail(@RequestParam String tid, @RequestParam String cid) {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(
                Message.builder()
                        .data(kakaoPayService.getOrderDetail(tid, cid))
                        .message(ORDER_INFO)
                        .build());
    }

    @PostMapping
    @Auth(role = Auth.Role.BASIC_USER)
    public ResponseEntity<Message> orderAction(@RequestBody OrderProductDTO orderProductDTO,
                                               HttpServletRequest request) throws RunOutOfStockException {

        String tokenUserId = request.getAttribute("tokenUserId").toString();
        String requestUrl = request.getRequestURL().toString().replace(request.getRequestURI(), "");

        String url = kakaoPayService.getKakaoPayUrl(orderProductDTO, tokenUserId, requestUrl);

        if (url == null) {
            getFailedPayMessage();
        }

        return ResponseEntity.ok().body(
                Message.builder()
                        .data(url)
                        .message(PAY_URI_MSG)
                        .build());
    }

    @PostMapping("/cart")
    @Auth(role = Auth.Role.BASIC_USER)
    public ResponseEntity<Message> cartOrderAction(HttpServletRequest request) throws UnsupportedEncodingException, RunOutOfStockException {
        Cookie[] cookies = request.getCookies();
        Cookie cartCookie = cartService.getCartCookie(cookies);

        if (cartCookie == null) {
            return ResponseEntity.badRequest().body(
                    Message.builder()
                            .message(EMPTY_CART_DATA)
                            .build()
            );
        }

        Integer[] productNoArr = CookieUtil.getItemNoArr(cartCookie);
        String[] productNameArr = CookieUtil.getItemNameArr(cartCookie);
        Integer[] productStockArr = CookieUtil.getStockArr(cartCookie);
        int totalAmount = CookieUtil.getTotalAmount(cartCookie);

        String tokenUserId = request.getAttribute("tokenUserId").toString();
        String requestUrl = request.getRequestURL().toString().replace(request.getRequestURI(), "");

        String url = kakaoPayService.getCartKakaoPayUrl(productNoArr, productNameArr,
                productStockArr, totalAmount, tokenUserId, requestUrl);

        if (url == null) {
            return getFailedPayMessage();
        }

        return ResponseEntity.ok().body(
                Message.builder()
                        .data(url)
                        .message(PAY_URI_MSG)
                        .build()
        );
    }


    @GetMapping("/completed")
    @Auth(role = Auth.Role.BASIC_USER)
    public ResponseEntity<Message> paySuccessAction(@RequestParam("pg_token") String pgToken) {
        PayApprovalDTO payInfo = kakaoPayService.getApprovedKakaoPayInfo(pgToken);

        if (payInfo == null) {
            getFailedPayMessage();
        }

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(
                Message.builder()
                        .data(payInfo)
                        .message(INFO_URI_MSG)
                        .build()
        );
    }

    @GetMapping("/cancel")
    @Auth(role = Auth.Role.BASIC_USER)
    public ResponseEntity<String> payCancelAction() {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(CANCELED_PAY_MESSAGE);
    }

    @GetMapping("/fail")
    @Auth(role = Auth.Role.BASIC_USER)
    public ResponseEntity<String> payFailAction() {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(FAILED_PAY_MESSAGE);
    }

    private ResponseEntity<Message> getFailedPayMessage() {
        return ResponseEntity.badRequest().body(
                Message.builder()
                        .message(FAILED_INFO_MESSAGE + "<br>" + INVALID_PARAMS)
                        .build()
        );
    }

}
