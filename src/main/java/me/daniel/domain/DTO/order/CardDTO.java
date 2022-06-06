package me.daniel.domain.DTO.order;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class CardDTO {

    private String purchaseCorp, purchaseCorpCode;
    private String issuerCorp, issuerCorpCode;
    private String bin, cardType, installMonth, approvedId, cardMid;
    private String interestFreeInstall, cardItemCode;

    public CardDTO() {
    }

    public CardDTO(String purchaseCorp, String purchaseCorpCode, String issuerCorp, String issuerCorpCode, String bin, String cardType, String installMonth, String approvedId, String cardMid, String interestFreeInstall, String cardItemCode) {
        this.purchaseCorp = purchaseCorp;
        this.purchaseCorpCode = purchaseCorpCode;
        this.issuerCorp = issuerCorp;
        this.issuerCorpCode = issuerCorpCode;
        this.bin = bin;
        this.cardType = cardType;
        this.installMonth = installMonth;
        this.approvedId = approvedId;
        this.cardMid = cardMid;
        this.interestFreeInstall = interestFreeInstall;
        this.cardItemCode = cardItemCode;
    }

    public String getPurchaseCorp() {
        return purchaseCorp;
    }

    public String getPurchaseCorpCode() {
        return purchaseCorpCode;
    }

    public String getIssuerCorp() {
        return issuerCorp;
    }

    public String getIssuerCorpCode() {
        return issuerCorpCode;
    }

    public String getBin() {
        return bin;
    }

    public String getCardType() {
        return cardType;
    }

    public String getInstallMonth() {
        return installMonth;
    }

    public String getApprovedId() {
        return approvedId;
    }

    public String getCardMid() {
        return cardMid;
    }

    public String getInterestFreeInstall() {
        return interestFreeInstall;
    }

    public String getCardItemCode() {
        return cardItemCode;
    }
}
