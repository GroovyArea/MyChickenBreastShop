package com.daniel.service;

import com.daniel.domain.dto.order.request.OrderProductDTO;
import com.daniel.domain.dto.order.request.PayCancelDTO;
import com.daniel.domain.dto.order.response.OrderCancelDTO;
import com.daniel.domain.dto.order.response.OrderInfoDTO;
import com.daniel.domain.dto.order.response.PayApprovalDTO;
import com.daniel.domain.dto.order.response.PayReadyDTO;
import com.daniel.domain.VO.AmountVO;
import com.daniel.domain.VO.CardVO;
import com.daniel.domain.VO.OrderVO;
import com.daniel.domain.VO.UserVO;
import com.daniel.exceptions.error.RunOutOfStockException;
import com.daniel.mapper.*;
import com.daniel.outbox.event.OrderCreated;
import com.daniel.outbox.event.OutBoxEventBuilder;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 주문 서비스 <br>
 * 카카오 페이 API 활용하여 카카오 서버에 결제 요청
 *
 * <pre>
 *     <b>History</b>
 *     1.0, 2022.05.28 최초 작성
 *     1.1, 2022.05.31 주문 조회 기능 추가
 *     1.2, 2022.06.02 동시성 문제 및 재고 차감 기능 추가
 * </pre>
 *
 * @author 김남영
 * @version 1.2
 */
@Service
@RequiredArgsConstructor
public class KakaoPayService {

    private static final String ORDER_APPROVED = "결제 승인";
    private static final String PARTNER_ORDER_ID = "partner_order_id";
    private static final String PARTNER_USER_ID = "partner_user_id";
    private static final String TOTAL_AMOUNT = "total_amount";

    @Value("${kakao.admin.key}")
    private String adminKey;
    @Value("${kakao.host}")
    private String host;
    @Value("${kakao.uri.approval}")
    private String approvalUri;
    @Value("${kakao.uri.cancel}")
    private String cancelUri;
    @Value("${kakao.uri.fail}")
    private String failUri;
    @Value("${kakao.pay.ready}")
    private String kakaoPayReady;
    @Value("${kakao.pay.approve}")
    private String kakaoPayApprove;
    @Value("${kakao.pay.cid}")
    private String testCid;
    @Value("${kakao.pay.taxfree}")
    private Integer taxFreeAmount;
    @Value("${kakao.pay.cancel}")
    private String kakaoPayCancel;
    @Value("${kakao.pay.order}")
    private String kakaoPayOrder;

    private static final Logger log = LoggerFactory.getLogger(KakaoPayService.class);

    private PayReadyDTO payReadyDTO;
    private RestTemplate restTemplate;
    private String orderId;
    private String userId;
    private String itemName;
    private Integer totalAmount;
    private UserVO user;
    private boolean exceptionFlag = true;

    private final ApplicationEventPublisher applicationEventPublisher;
    private final OutBoxEventBuilder<OrderCreated> outBoxEventOrderBuilder;
    private final OutBoxEventBuilder<List<OrderCreated>> outBoxEventCartBuilder;
    private final ModelMapper modelMapper;
    private final UserMapper userMapper;
    private final ProductMapper productMapper;
    private final CardMapper cardMapper;
    private final AmountMapper amountMapper;
    private final OrderMapper orderMapper;

    @Transactional
    public String getkakaoPayUrl(OrderProductDTO orderProductDTO, String tokenUserId, String requestUrl) throws RunOutOfStockException {

        /* 재고 확인 이벤트 발생 */
        applicationEventPublisher.publishEvent(
                outBoxEventOrderBuilder.createOutBoxEvent(OrderCreated.builder()
                        .itemNumber(orderProductDTO.getItemNumber())
                        .quantity(orderProductDTO.getQuantity())
                        .itemName(orderProductDTO.getItemName())
                        .totalAmount(orderProductDTO.getTotalAmount())
                        .build())
        );

        HttpHeaders headers = new HttpHeaders();
        setHeaders(headers);

        user = userMapper.selectUser(tokenUserId);
        orderId = user.getUserId() + " / " + orderProductDTO.getItemName();
        userId = user.getUserId();
        itemName = orderProductDTO.getItemName();
        totalAmount = orderProductDTO.getTotalAmount();

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        setParams(params, requestUrl);
        params.add(PARTNER_ORDER_ID, orderId);
        params.add(PARTNER_USER_ID, userId);
        params.add("item_name", itemName);
        params.add("quantity", String.valueOf(orderProductDTO.getQuantity()));
        params.add(TOTAL_AMOUNT, String.valueOf(totalAmount));
        params.add("tax_free_amount", String.valueOf(taxFreeAmount));

        /* 재고 품절 예외 발생 */
        if (!this.exceptionFlag) {
            throw new RunOutOfStockException();
        }

        int dbStock = productMapper.selectStockOfProduct(itemName);
        int updateStock = dbStock - orderProductDTO.getQuantity();

        /* 재고 차감 */
        updateStock(updateStock, itemName, "product_name");

        return getPayUrl(headers, params);
    }

    @Transactional
    public String getCartKakaoPayUrl(Integer[] productNoArr, String[] productNameArr, Integer[] productStockArr, int totalAmount,
                                     String tokenUserId, String requestUrl) throws RunOutOfStockException {

        List<OrderCreated> orderCreatedCartList =
                getOrderCreatedList(productNoArr, productNameArr, productStockArr, totalAmount);

        /* 재고 확인 이벤트 발생 */
        applicationEventPublisher.publishEvent(
                outBoxEventCartBuilder.createOutBoxEvent(orderCreatedCartList)
        );

        HttpHeaders headers = new HttpHeaders();
        setHeaders(headers);

        user = userMapper.selectUser(tokenUserId);
        itemName = productMapper.selectNoProduct(productNoArr[0]).getProductName() + " 그 외 " + (productNoArr.length - 1) + "개";
        orderId = user.getUserId() + ", " + itemName;
        userId = user.getUserId();
        this.totalAmount = totalAmount;

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        setParams(params, requestUrl);
        params.add(PARTNER_ORDER_ID, orderId);
        params.add(PARTNER_USER_ID, userId);
        params.add("item_name", itemName);
        params.add("item_code", String.join(", ",
                Arrays.stream(productNoArr)
                        .map(String::valueOf)
                        .toArray(String[]::new)));
        params.add("quantity", String.valueOf(productNoArr.length));
        params.add(TOTAL_AMOUNT, String.valueOf(totalAmount));
        params.add("tax_free_amount", String.valueOf(taxFreeAmount));

        /* 재고 품절 예외 발생 */
        if (!this.exceptionFlag) {
            throw new RunOutOfStockException();
        }

        /* 재고 차감 */
        IntStream.range(0, productStockArr.length).forEach(i -> {
            int dbStock = productMapper.selectStockOfProduct(productNameArr[i]);
            int updateStock = dbStock - productStockArr[i];
            updateStock(updateStock,
                    productNameArr[i], "product_name");
        });

        return getPayUrl(headers, params);
    }

    @Transactional
    public PayApprovalDTO getApprovedKakaoPayInfo(String pgToken) {
        HttpHeaders headers = new HttpHeaders();
        setHeaders(headers);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("cid", testCid);
        params.add("tid", payReadyDTO.getTid());
        params.add(PARTNER_ORDER_ID, orderId);
        params.add(PARTNER_USER_ID, userId);
        params.add("pg_token", pgToken);
        params.add(TOTAL_AMOUNT, String.valueOf(totalAmount));

        HttpEntity<MultiValueMap<String, String>> body = new HttpEntity<>(params, headers);

        PayApprovalDTO approvalDTO = restTemplate.postForObject(host + kakaoPayApprove, body, PayApprovalDTO.class);

        if (approvalDTO == null) {
            return null;
        }

        approvalDTO.setOrderStatus(ORDER_APPROVED);

        cardMapper.insertCard(modelMapper.map(approvalDTO.getCardInfo(), CardVO.class), approvalDTO.getTid());
        amountMapper.insertAmount(modelMapper.map(approvalDTO.getAmount(), AmountVO.class), approvalDTO.getTid());
        orderMapper.insertOrder(modelMapper.map(approvalDTO, OrderVO.class));

        try {
            return approvalDTO;
        } catch (RestClientException e) {
            log.error(e.getMessage());
        }

        return null;
    }

    @Transactional
    public OrderInfoDTO getOrderDetail(String tid, String cid) {
        HttpHeaders headers = new HttpHeaders();
        setHeaders(headers);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("cid", cid);
        params.add("tid", tid);

        HttpEntity<MultiValueMap<String, String>> body = new HttpEntity<>(params, headers);

        try {
            return restTemplate.postForObject(host + kakaoPayOrder, body, OrderInfoDTO.class);
        } catch (RestClientException e) {
            log.error(e.getMessage());
        }
        return null;
    }

    @Transactional
    public OrderCancelDTO cancelKakaoPay(PayCancelDTO payCancelDTO) {
        HttpHeaders headers = new HttpHeaders();
        setHeaders(headers);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("cid", testCid);
        params.add("tid", String.valueOf(payCancelDTO.getTid()));
        params.add("cancel_amount", String.valueOf(payCancelDTO.getCancelAmount()));
        params.add("cancel_tax_free_amount", String.valueOf(payCancelDTO.getCancelTaxFreeAmount()));

        HttpEntity<MultiValueMap<String, String>> body = new HttpEntity<>(params, headers);

        try {
            OrderCancelDTO responseDTO =
                    restTemplate.postForObject(host + kakaoPayCancel, body, OrderCancelDTO.class);

            if (responseDTO != null) {
                orderMapper.updateOrder(responseDTO.getTid());
            }

            return responseDTO;

        } catch (RestClientException e) {
            log.error(e.getMessage());
        }

        return null;
    }

    @Transactional
    public void updateStock(int productStock, String columnData, String searchColumn) {
        Map<String, Object> modifier = new HashMap<>();
        modifier.put("productStock", productStock);
        modifier.put("columnData", columnData);
        modifier.put("searchColumn", searchColumn);
        productMapper.updateStockOfProduct(modifier);
    }

    private void setHeaders(HttpHeaders headers) {
        restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

        headers.add("Authorization", "KakaoAK " + adminKey);
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.add("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE + ";charset=UTF-8");
    }

    private void setParams(MultiValueMap<String, String> params, String requestUrl) {
        params.add("cid", testCid);
        params.add("approval_url", requestUrl + approvalUri);
        params.add("cancel_url", requestUrl + cancelUri);
        params.add("fail_url", requestUrl + failUri);
    }

    private String getPayUrl(HttpHeaders headers, MultiValueMap<String, String> params) {
        HttpEntity<MultiValueMap<String, String>> body = new HttpEntity<>(params, headers);

        try {
            /* 서버 요청 후 응답 객체 받기 */
            payReadyDTO = restTemplate.postForObject(host + kakaoPayReady,
                    body, PayReadyDTO.class);

            return payReadyDTO != null ? payReadyDTO.getNextRedirectPcUrl() : null;
        } catch (RestClientException e) {
            log.error(e.getMessage());
        }
        return null;
    }

    private List<OrderCreated> getOrderCreatedList(Integer[] productNoArr, String[] productNameArr, Integer[] productStockArr, int totalAmount) {
        return IntStream.range(0, productNameArr.length).mapToObj(i -> OrderCreated.builder()
                .itemNumber(productNoArr[i])
                .quantity(productStockArr[i])
                .itemName(productNameArr[i])
                .totalAmount(totalAmount)
                .build()).collect(Collectors.toList());
    }

    public void changeStockFlag(boolean flag) {
        this.exceptionFlag = flag;
    }
}
