package com.daniel.domain.dto.order.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class OrderDTO {

    private String tid;
    private String userId;
    private String aid;
    private String cid;
    private String partnerOrderId;
    private String partnerUserId;
    private String paymentMethodType;
    private String itemName;
    private String itemCode;
    private Integer quantity;
    private String createdAt;
    private String approvedAt;
    private CardDTO cardDTO;
    private AmountDTO amountDTO;
    private String orderStatus;

    public void setCardDTO(CardDTO cardDTO) {
        this.cardDTO = cardDTO;
    }

    public void setAmountDTO(AmountDTO amountDTO) {
        this.amountDTO = amountDTO;
    }
}
