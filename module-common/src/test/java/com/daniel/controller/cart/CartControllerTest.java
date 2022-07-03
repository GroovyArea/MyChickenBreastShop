package com.daniel.controller.cart;

import com.daniel.domain.DTO.cart.CartItemDTO;
import com.daniel.enums.global.ResponseMessage;
import com.daniel.jwt.AuthorizationExtractor;
import com.daniel.jwt.JwtTokenProvider;
import com.daniel.service.CartService;
import com.daniel.service.ProductService;
import com.daniel.service.RedisService;
import com.daniel.utility.JsonUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.servlet.http.Cookie;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(RestDocumentationExtension.class)
@WebMvcTest(CartController.class)
@Import(value = {AuthorizationExtractor.class, JwtTokenProvider.class})
@AutoConfigureRestDocs
class CartControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CartService cartService;

    @MockBean
    ProductService productService;

    @MockBean
    RedisService redisService;

    @BeforeEach
    public void setUp(RestDocumentationContextProvider restDocumentationContextProvider) {
        mockMvc =
                MockMvcBuilders.standaloneSetup(new CartController(cartService, productService))
                        .apply(documentationConfiguration(restDocumentationContextProvider))
                        .addFilters(new CharacterEncodingFilter("utf-8", true))
                        .alwaysDo(document("{method-name}", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())))
                        .build();
    }

    CartItemDTO addCartDTO1 = CartItemDTO.builder()
            .productNo(9)
            .productName("스팀 오리지널")
            .productStock(5)
            .productPrice(65000).build();

    CartItemDTO addCartDTO2 = CartItemDTO.builder()
            .productNo(12)
            .productName("스팀 갈릭")
            .productStock(2)
            .productPrice(31000).build();

    CartItemDTO addCartDTO3 = CartItemDTO.builder()
            .productNo(13)
            .productName("스팀 데리야끼")
            .productStock(2)
            .productPrice(30000).build();

    CartItemDTO modifyCartDTO = CartItemDTO.builder()
            .productNo(13)
            .productName("스팀 데리야끼")
            .productStock(4)
            .productPrice(60000).build();

    CartItemDTO deleteCartDTO = CartItemDTO.builder()
            .productNo(13)
            .productName("스팀 데리야끼")
            .productStock(4)
            .productPrice(60000).build();

    Map<Integer, CartItemDTO> cartItemDTOMap = new HashMap<>();

    Cookie cookie1 = new Cookie("cookie1", "cookie1");
    Cookie cookie2 = new Cookie("cookie2", "cookie2");
    Cookie cookie3 = new Cookie("cookie3", "cookie3");
    Cookie cookie4 = new Cookie("cookie4", "cookie4");

    List<Cookie> cookieList = new ArrayList<>();

    Cookie newCartCookie;

    Cookie[] cookies;

    @BeforeEach
    void setUpCookie() {
        cartItemDTOMap.put(addCartDTO1.getProductNo(), addCartDTO1);
        cartItemDTOMap.put(addCartDTO2.getProductNo(), addCartDTO2);

        cookieList.add(cookie1);
        cookieList.add(cookie2);
        cookieList.add(cookie3);
        cookieList.add(cookie4);

        newCartCookie = new Cookie("Chicken", URLEncoder.encode(JsonUtil.objectToString(cartItemDTOMap), StandardCharsets.UTF_8));
    }

    @Test
    @DisplayName("장바구니 조회 테스트")
    void getCartListTest() throws Exception {

        Cookie newCartCookie = new Cookie("Chicken", URLEncoder.encode(JsonUtil.objectToString(cartItemDTOMap), StandardCharsets.UTF_8));

        newCartCookie.setMaxAge(60 * 60 * 24 * 7);
        newCartCookie.setPath("/api");

        cookieList.add(newCartCookie);

        cookies = cookieList.toArray(Cookie[]::new);


        Mockito.when(cartService.getCartCookie(cookies)).thenReturn(newCartCookie);
        Mockito.when(cartService.getCartDTOMap(newCartCookie)).thenReturn(cartItemDTOMap);

        mockMvc.perform(get("/api/carts")
                        .cookie(cookies))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").exists())
                .andExpect(jsonPath("$[1]").exists())
                .andDo(print());
    }

    @Test
    @DisplayName("장바구니 추가 테스트")
    void addCart() throws Exception {

        cartItemDTOMap.put(addCartDTO3.getProductNo(), addCartDTO3);

        cookieList.add(newCartCookie);

        cartItemDTOMap.put(addCartDTO3.getProductNo(), addCartDTO3);

        Cookie newCartCookie = new Cookie("Chicken", URLEncoder.encode(JsonUtil.objectToString(cartItemDTOMap), StandardCharsets.UTF_8));
        newCartCookie.setMaxAge(60 * 60 * 24 * 7);
        newCartCookie.setPath("/api");

        cookieList.add(newCartCookie);

        Cookie[] cookies = cookieList.toArray(Cookie[]::new);

        Mockito.when(cartService.getCartCookie(cookies)).thenReturn(newCartCookie);
        Mockito.when(cartService.getCartDTOMap(newCartCookie)).thenReturn(cartItemDTOMap);
        Mockito.when(cartService.resetCartCookie(newCartCookie, cartItemDTOMap)).thenReturn(newCartCookie);

        mockMvc.perform(post("/api/carts")
                        .cookie(cookies)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.objectToString(addCartDTO3)))
                .andExpect(status().isOk())
                .andExpect(content().string(ResponseMessage.ADD_MESSAGE.getValue()))
                .andDo(print());
    }

    @Test
    @DisplayName("장바구니 수정 테스트")
    void modifyCart() throws Exception {

        cartItemDTOMap.remove(addCartDTO3.getProductNo());
        cartItemDTOMap.put(modifyCartDTO.getProductNo(), modifyCartDTO);

        cookieList.add(newCartCookie);

        cartItemDTOMap.remove(addCartDTO3.getProductNo());
        cartItemDTOMap.put(modifyCartDTO.getProductNo(), modifyCartDTO);

        Cookie newCartCookie = new Cookie("Chicken", URLEncoder.encode(JsonUtil.objectToString(cartItemDTOMap), StandardCharsets.UTF_8));
        newCartCookie.setMaxAge(60 * 60 * 24 * 7);
        newCartCookie.setPath("/api");

        cookieList.add(newCartCookie);

        Cookie[] cookies = cookieList.toArray(Cookie[]::new);

        Mockito.when(cartService.getCartCookie(cookies)).thenReturn(newCartCookie);
        Mockito.when(cartService.getCartDTOMap(newCartCookie)).thenReturn(cartItemDTOMap);
        Mockito.when(cartService.resetCartCookie(newCartCookie, cartItemDTOMap)).thenReturn(newCartCookie);

        mockMvc.perform(put("/api/carts")
                        .cookie(cookies)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.objectToString(modifyCartDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string(ResponseMessage.MODIFY_MESSAGE.getValue()))
                .andDo(print());
    }

    @Test
    @DisplayName("장바구니 상품 삭제 테스트")
    void deleteCart() throws Exception {
        cartItemDTOMap.remove(deleteCartDTO.getProductNo());

        cookieList.add(newCartCookie);

        cartItemDTOMap.remove(deleteCartDTO.getProductNo());

        Cookie newCartCookie = new Cookie("Chicken", URLEncoder.encode(JsonUtil.objectToString(cartItemDTOMap), StandardCharsets.UTF_8));
        newCartCookie.setMaxAge(60 * 60 * 24 * 7);
        newCartCookie.setPath("/api");

        cookieList.add(newCartCookie);

        Cookie[] cookies = cookieList.toArray(Cookie[]::new);

        Mockito.when(cartService.getCartCookie(cookies)).thenReturn(newCartCookie);
        Mockito.when(cartService.getCartDTOMap(newCartCookie)).thenReturn(cartItemDTOMap);
        Mockito.when(cartService.resetCartCookie(newCartCookie, cartItemDTOMap)).thenReturn(newCartCookie);

        mockMvc.perform(delete("/api/carts")
                        .cookie(cookies)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.objectToString(deleteCartDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string(ResponseMessage.DELETE_MESSAGE.getValue()))
                .andDo(print());
    }
}