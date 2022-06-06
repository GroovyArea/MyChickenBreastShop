package me.daniel.domain.DTO.order;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PaymentActionDetailsDTO {

    private String aid, approvedAt, paymentActionType, payload;
    private Integer amount, pointAmount, discountAmount;
}
