package com.daniel.domain.dto.order.response;

import com.daniel.domain.dto.order.response.kakaopay.*;
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

    String cid;
    String cidSecret;
    String tid;
    String status;
    String partnerOrderId;
    String partnerUserId;
    String paymentMethodType;
    Amount amount;
    CanceledAmount canceledAmount;
    CancelAvailableAmount cancelAvailableAmount;
    String itemName;
    String itemCode;
    Integer quantity;
    Date createdAt;
    Date approvedAt;
    Date canceledAt;
    SelectedCardInfo selectedCardInfo;
    PaymentActionDetails[] paymentActionDetails;
}
