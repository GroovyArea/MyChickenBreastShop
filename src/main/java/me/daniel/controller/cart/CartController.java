package me.daniel.controller.cart;

import me.daniel.domain.DTO.CartItemDTO;
import me.daniel.enums.global.ResponseMessage;
import me.daniel.exceptions.NoCartException;
import me.daniel.interceptor.auth.Auth;
import me.daniel.responseMessage.Message;
import me.daniel.utility.CookieUtil;
import me.daniel.utility.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static me.daniel.utility.CookieUtil.getCartItemDTOMap;

/**
 * 장바구니 컨트롤러 <br>
 * 장바구니 조회, 추가, 수정, 삭제 처리를 실행한다.
 *
 * <pre>
 *     <b>History</b>
 *     김남영, 1.0, 2022.05.20 최초 작성
 * </pre>
 *
 * @author 김남영
 * @version 1.0
 */
@RestController
@RequestMapping("/api/carts")
public class CartController {

    private static final Logger log = LoggerFactory.getLogger(CartController.class);

    private static final String COOKIE_KEY = "Chicken";
    private static final String ENC_TYPE = "utf-8";
    private static final String CART_EMPTY = "장바구니가 비었습니다.";
    private static final String NULL_MODIFY_COOKIE = "변경하려는 장바구니의 상품 쿠키가 없습니다.";
    private static final String NULL_REMOVE_COOKIE = "삭제하려는 장바구니의 상품 쿠키가 없습니다.";

    /**
     * 장바구니 목록 조회 메서드
     *
     * @param request servlet Request
     * @return Message 장바구니 목록
     */
    @Auth(role = Auth.Role.BASIC_USER)
    @GetMapping
    public Message getCartList(HttpServletRequest request) throws UnsupportedEncodingException {
        Cookie[] requestCookies = request.getCookies();
        List<CartItemDTO> cartList;
        Cookie responseCookie;

        /* 쿠키가 존재할 때 카트 쿠키 반환 */
        if (requestCookies != null) {
            responseCookie = CookieUtil.getCartCookie(requestCookies);

            /* 장바구니 정보가 존재할 경우 */
            if (responseCookie != null) {
                String cookieValue = responseCookie.getValue();
                Map<Integer, CartItemDTO> cartDTOMap = JsonUtil.stringToMap(URLDecoder.decode(cookieValue, ENC_TYPE), Integer.class, CartItemDTO.class);

                if (cartDTOMap != null && !cartDTOMap.isEmpty()) {
                    cartList = new ArrayList<>(cartDTOMap.values());
                    return new Message
                            .Builder(cartList)
                            .httpStatus(HttpStatus.OK)
                            .mediaType(MediaType.APPLICATION_JSON)
                            .build();
                }
            }
        }
        return new Message
                .Builder(CART_EMPTY)
                .httpStatus(HttpStatus.NO_CONTENT)
                .build();
    }

    /**
     * 단일 상품 장바구니 추가 및 수정 메서드
     *
     * @param addCartDTO 추가, 수정할 장바구니 상품 데이터
     * @param request    servlet Request
     * @param response   servlet Response
     * @return 상태코드 & 메시지
     * @throws UnsupportedEncodingException url 인코드, 디코드 예외
     */
    @Auth(role = Auth.Role.BASIC_USER)
    @PostMapping
    public ResponseEntity<String> variateCart(@RequestBody(required = true) CartItemDTO addCartDTO, HttpServletRequest request,
                                              HttpServletResponse response) throws UnsupportedEncodingException {
        Integer productNo = addCartDTO.getProductNo();
        Cookie[] requestCookies = request.getCookies();
        Cookie responseCookie = null;

        /* 쿠키가 존재할 때 카트 쿠키 반환 */
        if (requestCookies != null) {
            responseCookie = CookieUtil.getCartCookie(requestCookies);
        }

        /* 응답 쿠키 없을 경우 쿠키 생성 */
        if (responseCookie == null) {
            responseCookie = new Cookie(COOKIE_KEY, URLEncoder.encode(JsonUtil.objectToString(new HashMap<Integer, CartItemDTO>()), ENC_TYPE));
            responseCookie.setPath("/api/carts");
        }

        Map<Integer, CartItemDTO> cartDTOMap = getCartItemDTOMap(responseCookie);

        /*
        기존 상품이 있을 경우 바꿔치기 해야됨 바뀔 데이터는 수량뿐
        상품 번호 = key, 상품 객체 = value
        */
        CartItemDTO cartItem = cartDTOMap.get(productNo);

        if (cartItem != null) {
            cartItem.setProductStock(addCartDTO.getProductStock() + cartItem.getProductStock());
        }

        cartDTOMap.put(productNo, cartItem);

        responseCookie.setValue(URLEncoder.encode(JsonUtil.objectToString(cartDTOMap), ENC_TYPE));
        response.addCookie(responseCookie);

        return ResponseEntity.ok().body(ResponseMessage.ADD_MESSAGE.getValue());
    }

    /**
     * 장바구니 상품 변경 메서드
     *
     * @param modifyCartDTO 변경할 카트 객체
     * @param request       Servlet Request 객체
     * @param response      Servlet Response 객체
     * @return 변경 메시지
     * @throws UnsupportedEncodingException 인코딩 문제 시 예외 발생
     * @throws NoCartException              쿠키 없을 때 예외 발생
     */
    @Auth(role = Auth.Role.BASIC_USER)
    @PutMapping
    public ResponseEntity<String> modifyCart(@RequestBody(required = true) CartItemDTO modifyCartDTO, HttpServletRequest request,
                                             HttpServletResponse response) throws UnsupportedEncodingException, NoCartException {
        Integer productNo = modifyCartDTO.getProductNo();
        Cookie[] requestCookies = request.getCookies();
        Cookie responseCookie = null;

        /* 쿠키가 존재할 때 카트 쿠키 반환 */
        if (requestCookies != null) {
            responseCookie = CookieUtil.getCartCookie(requestCookies);
        }

        if (responseCookie == null) {
            throw new NoCartException(NULL_MODIFY_COOKIE);
        }

        Map<Integer, CartItemDTO> cartDTOMap = CookieUtil.getCartItemDTOMap(responseCookie);

        /* 기존 상품 객체 삭제 */
        cartDTOMap.remove(productNo);

        /* 넘어온 상품 객체를 추가 */
        cartDTOMap.put(productNo, modifyCartDTO);

        responseCookie.setValue(URLEncoder.encode(JsonUtil.objectToString(cartDTOMap), ENC_TYPE));
        response.addCookie(responseCookie);

        return ResponseEntity.ok().body(ResponseMessage.MODIFY_MESSAGE.getValue());
    }

    /**
     * 장바구니 상품 삭제 메서드
     *
     * @param deleteCartDTO 삭제할 카트 객체
     * @param request       Servlet Request 객체
     * @param response      Servlet Response 객체
     * @return 삭제 메시지
     * @throws UnsupportedEncodingException 인코딩 문제 시 예외 발생
     * @throws NoCartException              쿠키 없을 때 예외 발생
     */
    @Auth(role = Auth.Role.BASIC_USER)
    @DeleteMapping
    public ResponseEntity<String> deleteCart(@RequestBody CartItemDTO deleteCartDTO, HttpServletRequest request,
                                             HttpServletResponse response) throws NoCartException, UnsupportedEncodingException {
        Integer productNo = deleteCartDTO.getProductNo();
        Cookie[] requestCookies = request.getCookies();
        Cookie responseCookie = null;

        if (requestCookies != null) {
            responseCookie = CookieUtil.getCartCookie(requestCookies);
        }

        if (responseCookie == null) {
            throw new NoCartException(NULL_REMOVE_COOKIE);
        }

        Map<Integer, CartItemDTO> cartDTOMap = CookieUtil.getCartItemDTOMap(responseCookie);

        /* 해당 상품 맵에서 삭제 */
        cartDTOMap.remove(productNo);

        responseCookie.setValue(URLEncoder.encode(JsonUtil.objectToString(cartDTOMap), ENC_TYPE));
        response.addCookie(responseCookie);

        return ResponseEntity.ok().body(ResponseMessage.DELETE_MESSAGE.getValue());
    }
}
