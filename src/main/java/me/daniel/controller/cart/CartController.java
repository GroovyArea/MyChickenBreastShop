package me.daniel.controller.cart;

import me.daniel.domain.DTO.CartItemDTO;
import me.daniel.enums.global.ResponseMessage;
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
import java.util.*;

import static me.daniel.utility.CookieUtil.getCartItemDTOMap;

/**
 * 장바구니 컨트롤러 <br>
 * 장바구니 조회, 추가, 수정, 삭제 처리를 실행한다.
 *
 * <pre>
 *     <b>History</b>
 *     김남영, 1.0, 2022.05.20 최초 작성
 *     김남영, 1.1, 2022.05.24 코드 리팩토링 (중복 제거 메서드 분리)
 * </pre>
 *
 * @author 김남영
 * @version 1.1
 */
@RestController
@RequestMapping("/api/carts")
public class CartController {

    private static final Logger log = LoggerFactory.getLogger(CartController.class);

    private static final String COOKIE_KEY = "Chicken";
    private static final String ENC_TYPE = "utf-8";
    private static final String CART_EMPTY = "장바구니가 비었습니다.";
    private static final String NULL_CART_COOKIE = "장바구니 쿠키가 없습니다.";
    private static final String NULL_MODIFY_COOKIE = "변경하려는 장바구니의 상품 쿠키가 없습니다.";
    private static final String NULL_REMOVE_COOKIE = "삭제하려는 장바구니의 상품 쿠키가 없습니다.";

    private Cookie responseCartCookie;
    private Map<Integer, CartItemDTO> cartDTOMap;
    private Integer productNo;

    /**
     * 장바구니 목록 조회 메서드
     *
     * @param request servlet Request
     * @return Message 장바구니 목록
     */
    @Auth(role = Auth.Role.BASIC_USER)
    @GetMapping
    public Message getCartList(HttpServletRequest request) throws UnsupportedEncodingException {
        getCartCookie(request);

        /* 장바구니 쿠키가 존재하지 않을 경우*/
        if (responseCartCookie == null) {
            return new Message
                    .Builder(NULL_CART_COOKIE)
                    .httpStatus(HttpStatus.NO_CONTENT)
                    .build();
        }

        cartDTOMap = JsonUtil.stringToMap(URLDecoder.decode(responseCartCookie.getValue(), ENC_TYPE), Integer.class, CartItemDTO.class);

        if (cartDTOMap != null && !cartDTOMap.isEmpty()) {
            List<CartItemDTO> cartList = new ArrayList<>(cartDTOMap.values());
            return new Message
                    .Builder(cartList)
                    .httpStatus(HttpStatus.OK)
                    .mediaType(MediaType.APPLICATION_JSON)
                    .build();
        }

        return new Message
                .Builder(CART_EMPTY)
                .httpStatus(HttpStatus.NO_CONTENT)
                .build();
    }

    /**
     * 단일 상품 장바구니 추가 메서드
     *
     * @param addCartDTO 추가할 장바구니 상품 데이터
     * @param response   servlet Response
     * @return 상태코드 & 메시지
     * @throws UnsupportedEncodingException url 인코드, 디코드 예외
     */
    @Auth(role = Auth.Role.BASIC_USER)
    @PostMapping
    public ResponseEntity<String> addCart(@RequestBody(required = true) CartItemDTO addCartDTO, HttpServletRequest request,
                                          HttpServletResponse response) throws UnsupportedEncodingException {
        productNo = addCartDTO.getProductNo();

        getCartCookie(request);

        /* 응답 쿠키 없을 경우 쿠키 생성 */
        if (responseCartCookie == null) {
            responseCartCookie = new Cookie(COOKIE_KEY, URLEncoder.encode(JsonUtil.objectToString(new HashMap<Integer, CartItemDTO>()), ENC_TYPE));
            responseCartCookie.setPath("/api/carts");
        }

        cartDTOMap = getCartItemDTOMap(responseCartCookie);

        /*
        기존 상품이 있을 경우 바꿔치기 해야됨 바뀔 데이터는 수량뿐
        상품 번호 = key, 상품 객체 = value
        */
        CartItemDTO cartItem = cartDTOMap.get(productNo);

        if (cartItem != null) {
            cartItem.setProductStock(addCartDTO.getProductStock() + cartItem.getProductStock());
        }

        cartDTOMap.put(productNo, cartItem);

        setCartCookie(response);

        return ResponseEntity.ok().body(ResponseMessage.ADD_MESSAGE.getValue());
    }

    /**
     * 장바구니 상품 변경 메서드
     *
     * @param modifyCartDTO 변경할 카트 객체
     * @param response      Servlet Response 객체
     * @return 변경 메시지
     * @throws UnsupportedEncodingException 인코딩 문제 시 예외 발생
     */
    @Auth(role = Auth.Role.BASIC_USER)
    @PutMapping
    public ResponseEntity<String> modifyCart(@RequestBody(required = true) CartItemDTO modifyCartDTO, HttpServletRequest request,
                                             HttpServletResponse response) throws UnsupportedEncodingException {
        productNo = modifyCartDTO.getProductNo();

        getCartCookie(request);

        if (responseCartCookie == null) {
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .body(NULL_MODIFY_COOKIE);
        }

        getCartDTOMap(responseCartCookie);

        removeProductFromMap(productNo);

        cartDTOMap.put(productNo, modifyCartDTO);

        setCartCookie(response);

        return ResponseEntity.ok().body(ResponseMessage.MODIFY_MESSAGE.getValue());
    }

    /**
     * 장바구니 상품 삭제 메서드
     *
     * @param deleteCartDTO 삭제할 카트 객체
     * @param response      Servlet Response 객체
     * @return 삭제 메시지
     * @throws UnsupportedEncodingException 인코딩 문제 시 예외 발생
     */
    @Auth(role = Auth.Role.BASIC_USER)
    @DeleteMapping
    public ResponseEntity<String> deleteCart(@RequestBody CartItemDTO deleteCartDTO, HttpServletRequest request,
                                             HttpServletResponse response) throws UnsupportedEncodingException {
        productNo = deleteCartDTO.getProductNo();

        getCartCookie(request);

        if (responseCartCookie == null) {
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .body(NULL_REMOVE_COOKIE);
        }

        getCartDTOMap(responseCartCookie);

        removeProductFromMap(productNo);

        setCartCookie(response);

        return ResponseEntity.ok().body(ResponseMessage.DELETE_MESSAGE.getValue());
    }

    /**
     * 장바구니 쿠키를 반환
     *
     * @param request servlet request 객체
     */
    private void getCartCookie(HttpServletRequest request) {
        responseCartCookie = CookieUtil.getCartCookie(request.getCookies()).orElse(null);
    }

    /**
     * 장바구니 쿠키 값에서 map 객체 추출
     *
     * @param responseCookie 장바구니 쿠키
     * @throws UnsupportedEncodingException 인코딩 문제 시 예외 발생
     */
    private void getCartDTOMap(Cookie responseCookie) throws UnsupportedEncodingException {
        cartDTOMap = CookieUtil.getCartItemDTOMap(responseCookie);
    }

    /**
     * 장바구니 map 객체에서 상품 삭제
     *
     * @param productNo 장바구니에 추가, 수정, 삭제할 상품 번호
     */
    private void removeProductFromMap(int productNo) {
        cartDTOMap.remove(productNo);
    }

    /**
     * 전달할 장바구니 쿠키를 세팅
     *
     * @param response servlet response 객체
     * @throws UnsupportedEncodingException 인코딩 문제 시 예외 발생
     */
    private void setCartCookie(HttpServletResponse response) throws UnsupportedEncodingException {
        responseCartCookie.setValue(URLEncoder.encode(JsonUtil.objectToString(cartDTOMap), ENC_TYPE));
        response.addCookie(responseCartCookie);
    }
}
