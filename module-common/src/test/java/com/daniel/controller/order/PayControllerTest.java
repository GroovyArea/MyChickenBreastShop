package com.daniel.controller.order;

import com.daniel.domain.DTO.order.request.PayCancelDTO;
import com.daniel.domain.DTO.order.response.*;
import com.daniel.domain.DTO.order.response.kakaoPay.Amount;
import com.daniel.domain.DTO.order.response.kakaoPay.ApprovedCancelAmount;
import com.daniel.domain.DTO.order.response.kakaoPay.CancelAvailableAmount;
import com.daniel.domain.DTO.order.response.kakaoPay.CanceledAmount;
import com.daniel.jwt.AuthorizationExtractor;
import com.daniel.jwt.JwtTokenProvider;
import com.daniel.service.KakaoPayService;
import com.daniel.service.RedisService;
import com.daniel.utility.JsonUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.Date;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@WebMvcTest(PayController.class)
@Import(value = {AuthorizationExtractor.class, JwtTokenProvider.class})
@AutoConfigureRestDocs
class PayControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    KakaoPayService kakaoPayService;

    @MockBean
    RedisService redisService;

    private final PayCancelDTO payCancelDTO = PayCancelDTO.builder()
            .tid("T2b3c5996520050855cc")
            .cancelAmount(96000)
            .cancelTaxFreeAmount(1000)
            .build();

    final Amount amountDTO = Amount.builder()
            .total(96000).taxFree(1000).vat(8636).point(10000).discount(0).build();

    final ApprovedCancelAmount approvedCancelAmount = ApprovedCancelAmount.builder()
            .total(96000).taxFree(1000).vat(8636).point(10000).discount(0).build();

    final CanceledAmount canceledAmount = CanceledAmount.builder()
            .total(96000).taxFree(1000).vat(8636).point(10000).discount(0).build();

    final CancelAvailableAmount cancelAvailableAmount = CancelAvailableAmount.builder()
            .total(96000).taxFree(1000).vat(8636).point(10000).discount(0).build();

    final OrderCancelDTO orderCancelDTO = OrderCancelDTO.builder()
            .cid("TC0ONETIME").tid("T2b5139b69eb7fb59eae").aid("A2b532a21d3e1c242f8b").status("CANCEL_PAYMENT")
            .partnerOrderId("aa11, 스팀 오리지널 그 외 1개").partnerUserId("aa11")
            .paymentMethodType("CARD").amount(amountDTO).approvedCancelAmount(approvedCancelAmount).canceledAmount(canceledAmount)
            .cancelAvailableAmount(cancelAvailableAmount).itemName("스팀 오리지널 그 외 1개").itemCode("9, 12")
            .quantity(2).createdAt(new Date()).approvedAt(new Date()).canceledAt(new Date()).build();

    @BeforeEach
    public void setUp(RestDocumentationContextProvider restDocumentationContextProvider) {
        mockMvc =
                MockMvcBuilders.standaloneSetup(new PayController(kakaoPayService))
                        .apply(documentationConfiguration(restDocumentationContextProvider))
                        .addFilters(new CharacterEncodingFilter("utf-8", true))
                        .alwaysDo(document("{method-name}", preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint())))
                        .build();
    }

    @DisplayName("결제 주문건 취소 테스트")
    @Test
    void payCancel() throws Exception {
        given(kakaoPayService.cancelKakaoPay(any(PayCancelDTO.class))).willReturn(orderCancelDTO);

        mockMvc.perform(post("/api/pay/cancel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.objectToString(payCancelDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.amount").exists())
                .andExpect(jsonPath("$.data.approved_cancel_amount").exists())
                .andExpect(jsonPath("$.data.approved_cancel_amount").exists())
                .andExpect(jsonPath("$.data.canceled_amount").exists())
                .andExpect(jsonPath("$.data.cancel_available_amount").exists())
                .andExpect(jsonPath("$.message", is("결제가 취소되었습니다.")))
                .andDo(print());
    }
}