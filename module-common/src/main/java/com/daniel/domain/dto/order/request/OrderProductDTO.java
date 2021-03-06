package com.daniel.domain.dto.order.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderProductDTO {

    private int quantity;

    private int itemNumber;

    private String itemName;

    private int totalAmount;
}
