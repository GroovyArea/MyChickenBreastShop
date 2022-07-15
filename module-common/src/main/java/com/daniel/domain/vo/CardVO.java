package com.daniel.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CardVO {

    private String tid;
    private String issuerCorp;
    private String issuerCorpCode;
    private String bin;
    private String cardType;
    private String installMonth;
    private String interestFreeInstall;
}
