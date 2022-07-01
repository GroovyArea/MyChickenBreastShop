package com.daniel.domain.DTO.order.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class OrderDTO {

    private String tid, userId, aid;
    private String cid, partnerOrderId, partnerUserId;
    private String paymentMethodType, itemName, itemCode;
    private Integer quantity;
    private String createdAt, approvedAt;
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
