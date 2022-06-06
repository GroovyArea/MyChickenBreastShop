package me.daniel.domain.DTO.order;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class CardDTO {

    private String purchaseCorp, purchaseCorpCode;
    private String issuerCorp, issuerCorpCode;
    private String bin, cardType, installMonth, approvedId, cardMid;
    private String interestFreeInstall, cardItemCode;
}
