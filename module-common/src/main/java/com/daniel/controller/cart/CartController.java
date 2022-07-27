package com.daniel.controller.cart;

import com.daniel.domain.dto.cart.CartItemDTO;
import com.daniel.enums.global.ResponseMessage;
import com.daniel.exceptions.error.InvalidPayAmountException;
import com.daniel.exceptions.error.InvalidProductException;
import com.daniel.interceptor.auth.Auth;
import com.daniel.service.CartService;
import com.daniel.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 장바구니 컨트롤러 <br>
 * 장바구니 조회, 추가, 수정, 삭제 처리를 실행한다.
 *
 * <pre>
 *     <b>History</b>
 *     김남영, 1.0, 2022.05.20 최초 작성
 *     김남영, 1.1, 2022.05.24 코드 리팩토링 (중복 제거 메서드 분리)
 *     김남영, 1.2, 2022.05.28 장바구니 데이터 유효성 검사
 *     김남영, 1.3, 2022.06.18 코드 리팩토링 (비즈니스 로직 Service Class로 분리)
 * </pre>
 *
 * @author 김남영
 * @version 1.3
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/carts")
public class CartController {

    //TODO 컨트롤러에 꼭 있어야 하는 값들인지 고민해보면 좋을거같아요
    private static final String CART_EMPTY = "장바구니가 비었습니다.";
    private static final String NULL_CART_COOKIE = "장바구니 쿠키가 없습니다.";
    private static final String NULL_MODIFY_COOKIE = "변경하려는 장바구니의 상품 쿠키가 없습니다.";
    private static final String NULL_REMOVE_COOKIE = "삭제하려는 장바구니의 상품 쿠키가 없습니다.";

    private final CartService cartService;
    private final ProductService productService;

    //TODO 빈으로 생성된 클래스들은 가변적인 상태를 갖고 있으면 안됩니다. 스레드 세이프하지 않아서 문제가 발생해요.
    // 아래 메소드들에서 아래 필드의 값을 변경하며 로직을 진행하는데 수정이 반드시 필요합니다.
    // 톰캣, 언더토우 등의 was가 멀티스레드 기반으로 동작한다는걸 생각해주세요.
    private Cookie responseCartCookie;
    private Map<Integer, CartItemDTO> cartDTOMap;
    private Integer productNo;

    /**
     * 장바구니 목록 조회 메서드
     *
     * @param request Servlet Request
     * @return 응답코드 & 장바구니 목록
     * @throws UnsupportedEncodingException url 인코드, 디코드 예외
     */
    @Auth(role = Auth.Role.BASIC_USER)
    @GetMapping
    public ResponseEntity<Object> getCartList(HttpServletRequest request) throws UnsupportedEncodingException { //TODO 체크드 익셉션은 서비스 레이어 안에서 처리해주세요

        responseCartCookie = cartService.getCartCookie(request.getCookies());

        /* 장바구니 쿠키가 존재하지 않을 경우*/
        if (responseCartCookie == null) {
            //TODO 아래 같은 응답은 Exception을 던져서 @RestControllerAdvice 같은 곳에서 공통적으로 처리하는게 좋아보이네요.
            return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON_UTF8).body(NULL_CART_COOKIE);
        }

        cartDTOMap = cartService.getCartDTOMap(responseCartCookie);

        //TODO 아래 같은 응답은 Exception을 던져서 @RestControllerAdvice 같은 곳에서 공통적으로 처리하는게 좋아보이네요.
        // 추가로 아래같은 null 체크는 Optional.ofNullable을 활용해보세요.
        if (cartDTOMap != null && !cartDTOMap.isEmpty()) {
            List<CartItemDTO> cartList = new ArrayList<>(cartDTOMap.values());
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(cartList);
        }

        //TODO 맥락상 ok 응답을 여기서 하고 , 위의 조건문에서 익셉션을 던지는게 더 잘 읽혔을거같아요
        return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON_UTF8).body(CART_EMPTY);
    }

    /**
     * 단일 상품 장바구니 추가 메서드
     *
     * @param addCartDTO 추가할 장바구니 상품 데이터
     * @param request    servlet Request
     * @param response   servlet Response
     * @return 상태코드 & 메시지
     * @throws UnsupportedEncodingException url 인코드, 디코드 예외
     * @throws InvalidProductException      상품 번호 오입력 예외
     * @throws InvalidPayAmountException    상품 총액 예외
     */
    @Auth(role = Auth.Role.BASIC_USER)
    @PostMapping
    public ResponseEntity<String> addCart(@RequestBody CartItemDTO addCartDTO, HttpServletRequest request,
                                          HttpServletResponse response) throws UnsupportedEncodingException, InvalidProductException, InvalidPayAmountException { //TODO 체크드 익셉션은 서비스 레이어 안에서 처리해주세요
        productNo = addCartDTO.getProductNo();

        cartValidate(addCartDTO); //TODO 서비스 레이어에서 처리해야할 내용이라고 보입니다. 컨트롤러는 이런 로직을 알 필요가 없어보여요. 컨트롤러의 책임은 무엇일까 한번 고민해주세요.

        responseCartCookie = cartService.getCartCookie(request.getCookies());

        //TODO getCartList() 메소드에서 코멘트 한 내용이랑 같습니다. 같은 관점으로 고민해주세요.

        /* 응답 장바구니 쿠키가 없을 경우 쿠키 생성 */
        if (responseCartCookie == null) {
            cartDTOMap = new HashMap<>();

            cartDTOMap.put(productNo, addCartDTO);

            responseCartCookie = cartService.createCartCookie(cartDTOMap);

            response.addCookie(responseCartCookie);

            return ResponseEntity.ok().body(ResponseMessage.ADD_MESSAGE.getValue());
        }

        cartDTOMap = cartService.getCartDTOMap(responseCartCookie);

        CartItemDTO cartItem = cartDTOMap.get(productNo);

        /* 기존 상품이 있을 경우 수량과 가격을 변경 후 응답 
        상품 번호 = key, 상품 객체 = value */
        if (cartItem != null) {
            cartItem.setProductStock(addCartDTO.getProductStock() + cartItem.getProductStock());
            cartItem.setProductPrice(addCartDTO.getProductPrice() + cartItem.getProductPrice());
            cartDTOMap.put(productNo, cartItem);
            responseNewCartCookie(response);
            return ResponseEntity.ok().body(ResponseMessage.ADD_MESSAGE.getValue());
        }

        cartDTOMap.put(productNo, addCartDTO);

        responseNewCartCookie(response);

        return ResponseEntity.ok().body(ResponseMessage.ADD_MESSAGE.getValue());
    }

    /**
     * 장바구니 상품 변경 메서드
     *
     * @param modifyCartDTO 변경할 카트 객체
     * @param request       Servlet Request
     * @param response      Servlet Response
     * @return 상태코드 & 메시지
     * @throws UnsupportedEncodingException url 인코드, 디코드 예외
     * @throws InvalidProductException      상품 번호 오입력 예외
     * @throws InvalidPayAmountException    상품 총액 예외
     */
    @Auth(role = Auth.Role.BASIC_USER)
    @PutMapping
    public ResponseEntity<String> modifyCart(@RequestBody CartItemDTO modifyCartDTO, HttpServletRequest request,
                                             HttpServletResponse response) throws UnsupportedEncodingException, InvalidProductException, InvalidPayAmountException {
        productNo = modifyCartDTO.getProductNo();

        cartValidate(modifyCartDTO);

        //TODO getCartList() 메소드에서 코멘트 한 내용이랑 같습니다. 같은 관점으로 고민해주세요.
        responseCartCookie = cartService.getCartCookie(request.getCookies());

        if (responseCartCookie == null) {
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .body(NULL_MODIFY_COOKIE);
        }

        cartDTOMap = cartService.getCartDTOMap(responseCartCookie);

        cartService.removeProductFromMap(productNo, cartDTOMap);

        cartDTOMap.put(productNo, modifyCartDTO);

        responseNewCartCookie(response);

        return ResponseEntity.ok().body(ResponseMessage.MODIFY_MESSAGE.getValue());
    }

    /**
     * 장바구니 상품 삭제 메서드
     *
     * @param deleteCartDTO 삭제할 장바구니 객체
     * @param request       Servlet Request
     * @param response      Servlet Response
     * @return 상태코드 & 메시지
     * @throws UnsupportedEncodingException url 인코드, 디코드 예외
     * @throws InvalidProductException      상품 번호 오입력 예외
     * @throws InvalidPayAmountException    상품 총액 예외
     */
    @Auth(role = Auth.Role.BASIC_USER)
    @DeleteMapping
    public ResponseEntity<String> deleteCart(@RequestBody CartItemDTO deleteCartDTO, HttpServletRequest request,
                                             HttpServletResponse response) throws UnsupportedEncodingException, InvalidProductException, InvalidPayAmountException {
        productNo = deleteCartDTO.getProductNo();

        cartValidate(deleteCartDTO);
        //TODO getCartList() 메소드에서 코멘트 한 내용이랑 같습니다. 같은 관점으로 고민해주세요.
        responseCartCookie = cartService.getCartCookie(request.getCookies());

        if (responseCartCookie == null) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(NULL_REMOVE_COOKIE);
        }

        cartDTOMap = cartService.getCartDTOMap(responseCartCookie);

        cartService.removeProductFromMap(productNo, cartDTOMap);

        responseNewCartCookie(response);

        return ResponseEntity.ok().body(ResponseMessage.DELETE_MESSAGE.getValue());
    }

    /**
     * 기존 장바구니 쿠키를 삭제하고 새롭게 만들어진 쿠키를 응답
     *
     * @param response servlet response 객체
     * @throws UnsupportedEncodingException 인코딩 문제 시 예외 발생
     */
    //TODO 코드의 추상화 수준이 들쭉날쭉해서 읽기 어렵게하는거 같습니다.
    // cartService가 이곳이 아니라 최소한 컨트롤러 메소드에 있어야 할거같아요
    private void responseNewCartCookie(HttpServletResponse response) throws UnsupportedEncodingException {
        Cookie newCookie = cartService.resetCartCookie(responseCartCookie, cartDTOMap);
        response.addCookie(newCookie);
    }

    /**
     * 장바구니 데이터 유효성 검사 <br>
     * 상품 유효성 검사 <br>
     * 총 가격 유효성 검사
     *
     * @param cartItemDTO 장바구니 객체
     */
    //TODO 코드의 추상화 수준이 들쭉날쭉해서 읽기 어렵게하는거 같습니다.
    // 코드의 재사용을 위해 아래처럼 메소드를 만든거라면, 차라리 productService.validate(productNo, cartItemDTO) 로 사용하는게 더 좋아보여요
    private void cartValidate(CartItemDTO cartItemDTO) throws InvalidProductException, InvalidPayAmountException { //TODO 마찬가지로 체크드 익셉션은 서비스 레이어에서 처리해주세요
        productService.validateProduct(productNo);
        productService.validatePayAmount(cartItemDTO);
    }
}
