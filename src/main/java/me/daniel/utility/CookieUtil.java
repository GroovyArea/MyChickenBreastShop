package me.daniel.utility;

import me.daniel.domain.DTO.CartItemDTO;

import javax.servlet.http.Cookie;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

/**
 * 쿠키 Utils <br>
 * 쿠키 이용에 필요한 util 메서드를 구현
 *
 * <pre>
 *     <b>History</b>
 *     김남영, 1.0, 2022.05.21 최초 작성
 * </pre>
 *
 * @author 김남영
 * @version 1.0
 */
public class CookieUtil {

    private static final String COOKIE_KEY = "Chicken";
    private static final String ENC_TYPE = "utf-8";

    /* 카트 쿠키 반환 메서드 */
    public static Cookie getCartCookie(Cookie[] requestCookies) {
        for (Cookie cookie : requestCookies) {
            if (COOKIE_KEY.equals(cookie.getName())) {
                return cookie;
            }
        }
        return null;
    }

    /* 카크 쿠키 값 디코딩 후 map 객체 반환 메서드 */
    public static Map<Integer, CartItemDTO> getCartItemDTOMap (Cookie responseCookie) throws UnsupportedEncodingException {
        String cookieValue = responseCookie.getValue();
        return JsonUtil.stringToMap(URLDecoder.decode(cookieValue, ENC_TYPE), Integer.class, CartItemDTO.class);
    }
}
