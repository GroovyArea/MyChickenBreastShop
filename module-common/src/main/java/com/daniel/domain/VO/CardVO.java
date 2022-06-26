package com.daniel.domain.VO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CardVO {

    private String tid;
    private String issuerCorp, issuerCorpCode;
    private String bin, cardType, installMonth;
    private String interestFreeInstall;
}
