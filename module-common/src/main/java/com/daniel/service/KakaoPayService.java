package com.daniel.service;

import com.daniel.domain.DTO.order.*;
import com.daniel.domain.VO.*;
import com.daniel.exceptions.RunOutOfStockException;
import com.daniel.mapper.*;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
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

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

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
public class KakaoPayService {

    private static final String RUN_OUT_OFF_STOCK = "해당 상품이 품절되었습니다.";

    @Value("${kakao.admin.key}")
    private String ADMIN_KEY;
    @Value("${kakao.host}")
    private String HOST;
    @Value("${kakao.uri.approval}")
    private String APPROVAL_URI;
    @Value("${kakao.uri.cancel}")
    private String CANCEL_URI;
    @Value("${kakao.uri.fail}")
    private String FAIL_URI;
    @Value("${kakao.pay.ready}")
    private String KAKAO_PAY_READY;
    @Value("${kakao.pay.approve}")
    private String KAKAO_PAY_APPROVE;
    @Value("${kakao.pay.cid}")
    private String TEST_CID;
    @Value("${kakao.pay.taxfree}")
    private Integer TAX_FREE_AMOUNT;
    @Value("${kakao.pay.cancel}")
    private String KAKAO_PAY_CANCEL;
    @Value("${kakao.pay.order}")
    private String KAKAP_PAY_ORDER;

    private static final Logger log = LoggerFactory.getLogger(KakaoPayService.class);

    private PayReadyDTO payReadyDTO;
    private RestTemplate restTemplate;
    private String orderId;
    private String userId;
    private String itemName;
    private Integer totalAmount;
    private UserVO user;

    private final ModelMapper modelMapper;
    private final UserMapper userMapper;
    private final ProductMapper productMapper;
    private final CardMapper cardMapper;
    private final AmountMapper amountMapper;
    private final OrderMapper orderMapper;

    public KakaoPayService(ModelMapper modelMapper, UserMapper userMapper, ProductMapper productMapper, CardMapper cardMapper, AmountMapper amountMapper, OrderMapper orderMapper) {
        this.modelMapper = modelMapper;
        this.userMapper = userMapper;
        this.productMapper = productMapper;
        this.cardMapper = cardMapper;
        this.amountMapper = amountMapper;
        this.orderMapper = orderMapper;
    }

    @Transactional
    public String getkakaoPayUrl(Map<String, Object> map, HttpServletRequest request) throws RunOutOfStockException {

        /* 재고 확인 */
        int productStock = productMapper.selectStockOfProduct(String.valueOf(map.get("itemName")));
        if (Integer.parseInt(map.get("quantity").toString()) > productStock) {
            throw new RunOutOfStockException(RUN_OUT_OFF_STOCK);
            //log.info("얌마");
        }

        /* 서버로 요청할 헤더*/
        HttpHeaders headers = new HttpHeaders();
        setHeaders(headers);

        user = userMapper.selectUser(request.getAttribute("tokenUserId").toString());
        orderId = user.getUserId() + " / " + map.get("itemName");
        userId = user.getUserId();
        itemName = String.valueOf(map.get("itemName"));
        totalAmount = Integer.valueOf(map.get("totalAmount").toString());

        /* 서버로 요청할 body */
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        setParams(params, request);
        params.add("partner_order_id", orderId);
        params.add("partner_user_id", userId);
        params.add("item_name", itemName);
        params.add("quantity", String.valueOf(map.get("quantity").toString()));
        params.add("total_amount", String.valueOf(totalAmount));
        params.add("tax_free_amount", String.valueOf(TAX_FREE_AMOUNT));

        /* 재고 차감 */
        updateStock(productStock - Integer.parseInt(map.get("quantity").toString()), String.valueOf(map.get("itemName")), "product_name");

        return getPayUrl(headers, params);
    }

    @Transactional
    public String getCartKakaoPayUrl(String[] productNoArr, Integer[] productStockArr, int totalAmount, HttpServletRequest request) throws RunOutOfStockException {

        /* 재고 확인 */
        for (int i = 0; i < productNoArr.length; i++) {
            ProductVO product = productMapper.selectNoProduct(Integer.parseInt(productNoArr[i]));
            if (productStockArr[i] > productMapper.selectStockOfProduct(product.getProductName())) {
                throw new RunOutOfStockException(RUN_OUT_OFF_STOCK);
            }
        }

        /* 서버로 요청할 헤더*/
        HttpHeaders headers = new HttpHeaders();
        setHeaders(headers);

        user = userMapper.selectUser((String) request.getAttribute("tokenUserId"));
        itemName = productMapper.selectNoProduct(Integer.parseInt(productNoArr[0])).getProductName() + " 그 외 " + (productNoArr.length - 1) + "개";
        orderId = user.getUserId() + ", " + itemName;
        userId = user.getUserId();
        this.totalAmount = totalAmount;

        /* 서버로 요청할 body */
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        setParams(params, request);
        params.add("partner_order_id", orderId);
        params.add("partner_user_id", userId);
        params.add("item_name", itemName);
        params.add("item_code", String.join(", ", productNoArr));
        params.add("quantity", String.valueOf(productNoArr.length));
        params.add("total_amount", String.valueOf(totalAmount));
        params.add("tax_free_amount", String.valueOf(TAX_FREE_AMOUNT));

        /* 재고 차감 */
        for (int i = 0; i < productNoArr.length; i++) {
            ProductVO product = productMapper.selectNoProduct(Integer.parseInt(productNoArr[i]));
            int stockToChange = product.getProductStock() - productStockArr[i];
            updateStock(stockToChange,
                    product.getProductName(), "product_name");
        }

        return getPayUrl(headers, params);
    }

    @Transactional
    public PayApprovalDTO getKakaoPayInfo(String pg_token) {

        /* 서버로 요청할 헤더*/
        HttpHeaders headers = new HttpHeaders();
        setHeaders(headers);

        /* 서버로 요청할 Body */
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("cid", TEST_CID);
        params.add("tid", payReadyDTO.getTid());
        params.add("partner_order_id", orderId);
        params.add("partner_user_id", userId);
        params.add("pg_token", pg_token);
        params.add("total_amount", String.valueOf(totalAmount));

        HttpEntity<MultiValueMap<String, String>> body = new HttpEntity<>(params, headers);

        PayApprovalDTO approvalDTO = restTemplate.postForObject(HOST + KAKAO_PAY_APPROVE, body, PayApprovalDTO.class);
        approvalDTO.setOrderStatus("결제 승인");

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

        /* 서버로 요청할 헤더*/
        HttpHeaders headers = new HttpHeaders();
        setHeaders(headers);

        /* 서버로 요청할 Body */
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("cid", cid);
        params.add("tid", tid);

        HttpEntity<MultiValueMap<String, String>> body = new HttpEntity<>(params, headers);

        try {
            return restTemplate.postForObject(HOST + KAKAP_PAY_ORDER, body, OrderInfoDTO.class);
        } catch (RestClientException e) {
            log.error(e.getMessage());
        }
        return null;
    }

    @Transactional
    public OrderCancelDTO cancelKakaoPay(Map<String, Object> map) {

        /* 서버로 요청할 헤더*/
        HttpHeaders headers = new HttpHeaders();
        setHeaders(headers);

        /* 서버로 요청할 Body */
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("tid", String.valueOf(map.get("tid")));
        params.add("cancel_amount", String.valueOf(map.get("cancelAmount")));
        params.add("cancel_tax_free_amount", String.valueOf(map.get("cancelTaxFreeAmount")));
        params.add("cid", TEST_CID);

        HttpEntity<MultiValueMap<String, String>> body = new HttpEntity<>(params, headers);

        try {
            OrderCancelDTO responseDTO =
                    restTemplate.postForObject(HOST + KAKAO_PAY_CANCEL, body, OrderCancelDTO.class);
            orderMapper.updateOrder(responseDTO.getTid());
            return responseDTO;

        } catch (RestClientException e) {
            log.error(e.getMessage());
        }

        return null;
    }


    private void setHeaders(HttpHeaders headers) {
        restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

        headers.add("Authorization", "KakaoAK " + ADMIN_KEY);
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.add("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE + ";charset=UTF-8");
    }

    private void setParams(MultiValueMap<String, String> params, HttpServletRequest request) {
        params.add("cid", TEST_CID);
        params.add("approval_url", getUrl(request) + APPROVAL_URI);
        params.add("cancel_url", getUrl(request) + CANCEL_URI);
        params.add("fail_url", getUrl(request) + FAIL_URI);
    }

    private void updateStock(int productStock, String columnData, String searchColumn) {
        Map<String, Object> modifier = new HashMap<>();
        modifier.put("productStock", productStock);
        modifier.put("columnData", columnData);
        modifier.put("searchColumn", searchColumn);
        productMapper.updateStockOfProduct(modifier);
    }

    private String getPayUrl(HttpHeaders headers, MultiValueMap<String, String> params) {
        HttpEntity<MultiValueMap<String, String>> body = new HttpEntity<>(params, headers);

        try {
            /* 서버 요청 후 응답 객체 받기 */
            payReadyDTO = restTemplate.postForObject(HOST + KAKAO_PAY_READY,
                    body, PayReadyDTO.class);

            return payReadyDTO != null ? payReadyDTO.getNextRedirectPcUrl() : null;
        } catch (RestClientException e) {
            log.error(e.getMessage());
        }
        return null;
    }

    private String getUrl(HttpServletRequest request) {
        return request.getRequestURL().toString().replace(request.getRequestURI(), "");
    }
}
