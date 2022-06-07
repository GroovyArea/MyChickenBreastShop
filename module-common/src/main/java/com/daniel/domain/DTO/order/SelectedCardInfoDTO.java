package com.daniel.domain.DTO.order;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SelectedCardInfoDTO {

    private String cardBin, cardCorpName, interestFreeInstall;
    private Integer installMonth;
}
