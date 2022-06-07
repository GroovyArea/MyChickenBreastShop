package com.daniel.domain.DTO.order;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class CancelAvailableAmountDTO {

    Integer total, taxFree, vat, point, discount;
}
