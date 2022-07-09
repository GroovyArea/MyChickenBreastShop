package com.daniel.domain.DTO.order.response.kakaoPay;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@Builder
public class SelectedCardInfo {

    private String cardBin;
    private String cardCorpName;
    private String interestFreeInstall;
    private Integer installMonth;
}
