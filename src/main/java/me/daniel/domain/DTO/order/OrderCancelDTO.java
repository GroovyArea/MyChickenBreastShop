package me.daniel.domain.DTO.order;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class OrderCancelDTO {

    private String cid, tid, aid, status;
    private Integer cancelAmount, cancelTaxFreeAmount;
    private String partnerOrderId, partnerUserId, paymentMethodType;
    private AmountDTO amountDTO;
    private ApprovedCancelAmountDTO approvedCancelAmountDTO;
    private CanceledAmountDTO canceledAmountDTO;
    private CancelAvailableAmountDTO cancelAvailableAmountDTO;
    private String itemName, itemCode;
    private Integer quantity;
    private Date createdAt, approvedAt, canceledAt;
}
