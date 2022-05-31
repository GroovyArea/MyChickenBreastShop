package me.daniel.service;

import me.daniel.domain.DTO.OrderDTO;
import me.daniel.domain.DTO.UserDTO;
import me.daniel.domain.DTO.KakaoPayApprovalDTO;
import me.daniel.domain.DTO.KakaoPayReadyDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;

/**
 * 주문 서비스 <br>
 * 카카오 페이 API 활용하여 카카오 서버에 결제 요청
 *
 * <pre>
 *     <b>History</b>
 *     김남영, 1.0, 2022.05.28 최초 작성
 * </pre>
 *
 * @author 김남영
 * @version 1.0
 */
@Service
public class KakaoPayService {

    @Value("${admin.key}")
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

    private static final Logger log = LoggerFactory.getLogger(KakaoPayService.class);

    private KakaoPayReadyDTO kakaoPayReadyDTO;
    private RestTemplate restTemplate;
    private String orderId;
    private String userId;
    private String itemName;
    private Integer totalAmount;
    private UserDTO user;

    private final UserService userService;
    private final ProductService productService;

    public KakaoPayService(UserService userService, ProductService productService) {
        this.userService = userService;
        this.productService = productService;
    }

    public String getkakaoPayUrl(OrderDTO orderDTO, HttpServletRequest request) {

        /* 서버로 요청할 헤더*/
        HttpHeaders headers = new HttpHeaders();
        setHeaders(headers);

        user = userService.findById((String) request.getAttribute("tokenUserId"));
        orderId = user.getUserId() + orderDTO.getItemName();
        userId = user.getUserId();
        itemName = orderDTO.getItemName();
        totalAmount = orderDTO.getTotalAmount();

        /* 서버로 요청할 body */
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        setParams(params, request);
        params.add("partner_order_id", orderId);
        params.add("partner_user_id", userId);
        params.add("item_name", itemName);
        params.add("quantity", String.valueOf(orderDTO.getQuantity()));
        params.add("total_amount", String.valueOf(totalAmount));
        params.add("tax_free_amount", String.valueOf(TAX_FREE_AMOUNT));

        return getPayUrl(headers, params);
    }

    public String getCartKakaoPayUrl(String[] productNoArr, HttpServletRequest request, int totalAmount) {

        /* 서버로 요청할 헤더*/
        HttpHeaders headers = new HttpHeaders();
        setHeaders(headers);

        user = userService.findById((String) request.getAttribute("tokenUserId"));
        itemName = productService.findByNumber(Integer.parseInt(productNoArr[0])).getProductName() + " 그 외 " + (productNoArr.length - 1) + "개";
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

        return getPayUrl(headers, params);
    }

    public KakaoPayApprovalDTO getKakaoPayInfo(String pg_token, String jwtToken) {

        log.info("오냐?");
        /* 서버로 요청할 헤더*/
        HttpHeaders headers = new HttpHeaders();
        setHeaders(headers);
        headers.add("AuthorizationToken", "Bearer " + jwtToken);

        // 서버로 요청할 Body
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("cid", TEST_CID);
        params.add("tid", kakaoPayReadyDTO.getTid());
        params.add("partner_order_id", orderId);
        params.add("partner_user_id", userId);
        params.add("pg_token", pg_token);
        params.add("total_amount", String.valueOf(totalAmount));

        HttpEntity<MultiValueMap<String, String>> body = new HttpEntity<>(params, headers);

        try {
            return restTemplate.postForObject(HOST + KAKAO_PAY_APPROVE, body, KakaoPayApprovalDTO.class);
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

    private String getPayUrl(HttpHeaders headers, MultiValueMap<String, String> params) {
        HttpEntity<MultiValueMap<String, String>> body = new HttpEntity<>(params, headers);

        try {
            /* 서버 요청 후 응답 객체 받기 */
            kakaoPayReadyDTO = restTemplate.postForObject(HOST + KAKAO_PAY_READY,
                    body, KakaoPayReadyDTO.class);

            return kakaoPayReadyDTO != null ? kakaoPayReadyDTO.getNext_redirect_pc_url() : null;
        } catch (RestClientException e) {
            log.error(e.getMessage());
        }
        return null;
    }

    private String getUrl(HttpServletRequest request) {
        return request.getRequestURL().toString().replace(request.getRequestURI(), "");
    }
}
