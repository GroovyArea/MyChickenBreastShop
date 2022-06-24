package com.daniel.domain.DTO.order.response;

import com.daniel.domain.DTO.order.response.kakaoPay.ApprovedCancelAmount;
import com.daniel.domain.DTO.order.response.kakaoPay.CancelAvailableAmount;
import com.daniel.domain.DTO.order.response.kakaoPay.CanceledAmount;
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
public class OrderCancelDTO {

    private String cid, tid, aid, status;
    private String partnerOrderId, partnerUserId, paymentMethodType;
    private AmountDTO amount;
    private ApprovedCancelAmount approvedCancelAmount;
    private CanceledAmount canceledAmount;
    private CancelAvailableAmount cancelAvailableAmount;
    private String itemName, itemCode;
    private Integer quantity;
    private Date createdAt, approvedAt, canceledAt;
}
