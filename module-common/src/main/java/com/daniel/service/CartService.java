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
        //TODO Optional은 null safe한 코드를 지향하기 위해 사용하는데, 리턴값으로 null을 사용한다는거는 한번 더 고민해주세요.
        return CookieUtil.getCartCookie(cookies).orElse(null);
    }

    //TODO 유틸함수도 아닌 서비스 클래스가 반환형으로 Map을 사용하는건 좋지 않아보여요. 타입추론이 어려워집니다
    public Map<Integer, CartItemDTO> getCartDTOMap(Cookie responseCartCookie) throws UnsupportedEncodingException { //TODO 체크드 에러는 try - catch해서 서비스 레이어에서 다루는 커스텀 익셉션으로 다시 전파시키는게 적절해보입니다.
        return CookieUtil.getCartItemDTOMap(responseCartCookie);
    }

    public void removeProductFromMap(int productNo, Map<Integer, CartItemDTO> cartItemDTOMap) {
        cartItemDTOMap.remove(productNo);
    }

    //TODO 서비스 클래스의 리턴타입이 javax.servlet.http 관련 클래스이네요. 최소한 서비스 레이어에서 사용하는 모델을 만들어서 래핑해주세요.
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
