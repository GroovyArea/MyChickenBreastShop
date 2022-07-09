package com.daniel.domain.DTO.order.response.kakaoPay;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@Builder
public class CancelAvailableAmount {

    Integer total;
    Integer taxFree;
    Integer vat;
    Integer point;
    Integer discount;
}
