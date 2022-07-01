package com.daniel.domain.DTO.order.response;

import com.daniel.domain.DTO.order.response.kakaoPay.*;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class OrderInfoDTO {

    String cid, cidSecret, tid;
    String status, partnerOrderId, partnerUserId, paymentMethodType;
    Amount amount;
    CanceledAmount canceledAmount;
    CancelAvailableAmount cancelAvailableAmount;
    String itemName, itemCode;
    Integer quantity;
    Date createdAt, approvedAt, canceledAt;
    SelectedCardInfo selectedCardInfo;
    PaymentActionDetails[] paymentActionDetails;
}
