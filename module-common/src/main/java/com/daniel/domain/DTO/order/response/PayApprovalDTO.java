package com.daniel.domain.DTO.order.response;

import com.daniel.domain.DTO.order.response.kakaoPay.Amount;
import com.daniel.domain.DTO.order.response.kakaoPay.CardInfo;
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
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@Builder
public class PayApprovalDTO {

    private String aid, tid, cid, sid;
    private String partnerOrderId, partnerUserId, paymentMethodType;
    private Amount amount;
    private CardInfo cardInfo;
    private String itemName, itemCode, payload;
    private Integer quantity, taxFreeAmount, vatAmount;
    private Date createdAt, approvedAt;
    private String orderStatus;

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
}
