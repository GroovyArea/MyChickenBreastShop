package com.daniel.domain.DTO.order.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class AmountDTO {

    private String tid;
    private Integer total, taxFree, vat, point, discount;
}
