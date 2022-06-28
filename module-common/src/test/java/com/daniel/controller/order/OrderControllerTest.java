package com.daniel.controller.order;

import com.daniel.domain.DTO.cart.CartItemDTO;
import com.daniel.domain.DTO.order.*;
import com.daniel.jwt.AuthorizationExtractor;
import com.daniel.jwt.JwtTokenProvider;
import com.daniel.service.CartService;
import com.daniel.service.KakaoPayService;
import com.daniel.service.OrderService;
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
import java.util.*;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(RestDocumentationExtension.class)
@WebMvcTest(OrderController.class)
@Import(value = {AuthorizationExtractor.class, JwtTokenProvider.class})
@AutoConfigureRestDocs
class OrderControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    OrderService orderService;

    @MockBean
    KakaoPayService kakaoPayService;

    @MockBean
    CartService cartService;

    @MockBean
    RedisService redisService;

    @BeforeEach
    public void setUp(RestDocumentationContextProvider restDocumentationContextProvider) {
        mockMvc =
                MockMvcBuilders.standaloneSetup(new OrderController(kakaoPayService, orderService, cartService))
                        .apply(documentationConfiguration(restDocumentationContextProvider))
                        .addFilters(new CharacterEncodingFilter("utf-8", true))
                        .alwaysDo(document("{method-name}", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())))
                        .build();
    }

    AmountDTO amountDTO = AmountDTO.builder()
            .total(96000).taxFree(1000).vat(8636).point(0).discount(0).build();

    OrderInfoDTO orderInfoDTO = OrderInfoDTO.builder()
            .cid("TC0ONETIME").cidSecret(null).tid("T2b3092b6520050854a7").status("결제 승인")
            .partnerOrderId("aa11, 스팀 오리지널 그 외 1개").partnerUserId("aa11").paymentMethodType("CARD")
            .amountDTO(amountDTO)
            .canceledAmountDTO(null)
            .cancelAvailableAmountDTO(null)
            .itemName("스팀 오리지널 그 외 1개").itemCode("9, 12").quantity(2)
            .createdAt(new java.util.Date()).approvedAt(new Date()).canceledAt(new Date())
            .selectedCardInfoDTO(null)
            .paymentActionDetails(null).build();

    CardDTO cardDTO = CardDTO.builder()
            .purchaseCorp("NH카드").purchaseCorpCode("11").issuerCorp("NH카드").issuerCorpCode("11")
            .bin("123456").cardType("신용").installMonth("00").approvedId("11111111").cardMid(null)
            .interestFreeInstall("N").cardItemCode(null).build();

    PayApprovalDTO payApprovalDTO = PayApprovalDTO.builder()
            .aid("A162b3c5b73b3844359a").tid("T2b3c5996520050855cc").cid("TC0ONETIME")
            .partnerOrderId("aa11, 스팀 오리지널 그 외 1개").partnerUserId("aa11").paymentMethodType("CARD")
            .itemName("스팀 오리지널 그 외 1개").itemCode("9, 12").quantity(2).createdAt(new Date()).approvedAt(new Date())
            .amount(amountDTO).cardInfo(cardDTO).build();

    OrderProductDTO orderProductDTO = OrderProductDTO.builder()
            .quantity(5).itemNumber(9).itemName("스팀 오리지널").totalAmount(65000).build();

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
        cartItemDTOMap.put(addCartDTO3.getProductNo(), addCartDTO3);

        cookieList.add(cookie1);
        cookieList.add(cookie2);
        cookieList.add(cookie3);
        cookieList.add(cookie4);

        newCartCookie = new Cookie("Chicken", URLEncoder.encode(JsonUtil.objectToString(cartItemDTOMap), StandardCharsets.UTF_8));
        newCartCookie.setMaxAge(60 * 60 * 24 * 7);
        newCartCookie.setPath("/api");

        cookieList.add(newCartCookie);
        cookies = cookieList.toArray(Cookie[]::new);
    }

    @Test
    @DisplayName("회원 주문 조회 리스트 테스트")
    void getDBOrderInfo() throws Exception {
        List<OrderInfoDTO> orderInfoDTOList = new ArrayList<>();
        orderInfoDTOList.add(orderInfoDTO);
        Mockito.when(orderService.getOrderInfoList(orderInfoDTO.getPartnerUserId())).thenReturn(orderInfoDTOList);

        mockMvc.perform(get("/api/order/{userId}", orderInfoDTO.getPartnerUserId())
                        .header("Authorization", "Bearer ${ADMIN_AUTH_TOKEN}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.message", is("회원 주문 조회 리스트입니다.")))
                .andDo(print());
    }

    @Test
    @DisplayName("주문 상세 정보 가져오기 테스트")
    void getOrderDetail() throws Exception {
        Mockito.when(kakaoPayService.getOrderDetail(orderInfoDTO.getTid(), orderInfoDTO.getCid())).thenReturn(orderInfoDTO);

        mockMvc.perform(get("/api/order/detail")
                        .header("Authorization", "Bearer ${ADMIN_AUTH_TOKEN}")
                        .param("tid", orderInfoDTO.getTid())
                        .param("cid", orderInfoDTO.getCid()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.message", is("주문 상세 조회입니다.")))
                .andDo(print());
    }

    @Test
    @DisplayName("단건 결제 테스트")
    void orderAction() throws Exception {

        String userId = orderInfoDTO.getPartnerUserId();
        String url = "https://online-pay.kakao.com/mockup/v1/cd749f82c8c58decb5c832ab45a0990e02c87b07e32bf3f28aa9b9297a0cf710/info";

        Mockito.when(kakaoPayService.getkakaoPayUrl(any(OrderProductDTO.class), anyString(), anyString())).thenReturn(url);

        mockMvc.perform(post("/api/order")
                        .header("Authorization", "Bearer ${ADMIN_AUTH_TOKEN}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.objectToString(orderProductDTO))
                        .requestAttr("tokenUserId", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", is(url)))
                .andExpect(jsonPath("$.message", is("카카오 페이 결제 URL")))
                .andDo(print());
    }

    @Test
    @DisplayName("장바구니 결제 테스트")
    void cartOrderAction() throws Exception {
        given(cartService.getCartCookie(cookies)).willReturn(newCartCookie);

        String userId = orderInfoDTO.getPartnerUserId();
        String url = "https://online-pay.kakao.com/mockup/v1/ce3cb801e74a337fb7cf74d9a9aa139bb569e74ba316ae7eb282b90f8f08a522/info";

        given(kakaoPayService.getCartKakaoPayUrl(any(Integer[].class), any(String[].class),
                any(Integer[].class), anyInt(), anyString(), anyString())).willReturn(url);

        mockMvc.perform(post("/api/order/cart")
                        .header("Authorization", "Bearer ${ADMIN_AUTH_TOKEN}")
                        .cookie(cookies)
                        .requestAttr("tokenUserId", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", is(url)))
                .andExpect(jsonPath("$.message", is("카카오 페이 결제 URL")))
                .andDo(print());
    }

    @Test
    @DisplayName("결제 성공 정보 조회 테스트")
    void paySuccessAction() throws Exception {
        String pg_token = "394ac990b110190422e4";
        Mockito.when(kakaoPayService.getKakaoPayInfo(pg_token)).thenReturn(payApprovalDTO);

        mockMvc.perform(get("/api/order/completed")
                        .header("Authorization", "Bearer ${ADMIN_AUTH_TOKEN}")
                        .param("pg_token", pg_token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.message", is("결제 정보 조회 목록입니다.")))
                .andDo(print());
    }

    @Test
    @DisplayName("결제 취소될 경우 리다이렉트 요청 테스트")
    void payCancelAction() throws Exception {
        mockMvc.perform(get("/api/order/cancel"))
                .andExpect(status().isOk())
                .andExpect(content().string("결제를 취소하셨습니다."))
                .andDo(print());
    }

    @Test
    @DisplayName("결제 실패할 경우 리다이렉트 요청 테스트")
    void payFailAction() throws Exception {
        mockMvc.perform(get("/api/order/fail"))
                .andExpect(status().isOk())
                .andExpect(content().string("결제 준비 및 조회에 실패했습니다."))
                .andDo(print());
    }
}