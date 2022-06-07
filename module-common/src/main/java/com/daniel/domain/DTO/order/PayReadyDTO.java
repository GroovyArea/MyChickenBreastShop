package com.daniel.domain.DTO.order;

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
public class PayReadyDTO {

    private String partnerOrderId, partnerUserId, itemName;
    private Integer quantity, totalAmount, taxFreeAmount;
    private String tid, nextRedirectPcUrl;
    private Date createdAt;
}
