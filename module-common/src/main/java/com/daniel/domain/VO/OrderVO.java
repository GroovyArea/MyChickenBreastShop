package com.daniel.domain.VO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class OrderVO {

    private String tid, userId, aid;
    private String cid, partnerOrderId, partnerUserId;
    private String paymentMethodType, itemName, itemCode;
    private Integer quantity;
    private String createdAt, approvedAt;
    private CardVO cardVO;
    private AmountVO amountVO;
    private String orderStatus;
}
