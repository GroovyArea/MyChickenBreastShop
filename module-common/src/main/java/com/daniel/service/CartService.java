package com.daniel.service;

import com.daniel.domain.dto.cart.CartItemDTO;
import com.daniel.utility.CookieUtil;
import com.daniel.utility.JsonUtil;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * 장바구니 서비스 <br>
 * 쿠키를 이용한 장바구니 컨트롤러의 비즈니스 로직 처리
 *
 * <pre>
 *     <b>History</b>
 *     1.0, 2022.06.18 최초 작성
 * </pre>
 *
 * @author 김남영
 * @version 1.0
 */
@Service
public class CartService {

    private static final String COOKIE_KEY = "Chicken";
    private static final String ENC_TYPE = "utf-8";
    private static final int COOKIE_AGE = 60 * 60 * 24 * 7;
    private static final int KILL_COOKIE = 0;

    public Cookie getCartCookie(Cookie[] cookies) {
        return CookieUtil.getCartCookie(cookies).orElse(null);
    }

    public Map<Integer, CartItemDTO> getCartDTOMap(Cookie responseCartCookie) throws UnsupportedEncodingException {
        return CookieUtil.getCartItemDTOMap(responseCartCookie);
    }

    public void removeProductFromMap(int productNo, Map<Integer, CartItemDTO> cartItemDTOMap) {
        cartItemDTOMap.remove(productNo);
    }

    public Cookie createCartCookie(Map<Integer, CartItemDTO> cartItemDTOMap) throws UnsupportedEncodingException {
        Cookie newCookie = new Cookie(COOKIE_KEY, URLEncoder.encode(JsonUtil.objectToString(cartItemDTOMap), ENC_TYPE));
        newCookie.setHttpOnly(true);
        newCookie.setSecure(true);
        newCookie.setPath("/api");
        newCookie.setMaxAge(COOKIE_AGE);
        return newCookie;
    }

    public Cookie resetCartCookie(Cookie responseCartCookie, Map<Integer, CartItemDTO> cartItemDTOMap) throws UnsupportedEncodingException {
        responseCartCookie.setMaxAge(KILL_COOKIE);
        return createCartCookie(cartItemDTOMap);
    }
}
