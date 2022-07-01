package com.daniel.domain.DTO.order.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CardDTO {

    private String tid;
    private String issuerCorp, issuerCorpCode;
    private String bin, cardType, installMonth;
    private String interestFreeInstall;

}
