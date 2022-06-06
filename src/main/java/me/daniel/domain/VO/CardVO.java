package me.daniel.domain.VO;

public class CardVO {

    private String tid, userId;
    private String issuerCorp, issuerCorpCode;
    private String bin, cardType, installMonth;
    private String interestFreeInstall;

    public CardVO() {
    }

    public CardVO(String tid, String userId, String issuerCorp, String issuerCorpCode, String bin, String cardType, String installMonth, String interestFreeInstall) {
        this.tid = tid;
        this.userId = userId;
        this.issuerCorp = issuerCorp;
        this.issuerCorpCode = issuerCorpCode;
        this.bin = bin;
        this.cardType = cardType;
        this.installMonth = installMonth;
        this.interestFreeInstall = interestFreeInstall;
    }

    public String getTid() {
        return tid;
    }

    public String getUserId() {
        return userId;
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

    public String getInterestFreeInstall() {
        return interestFreeInstall;
    }
}
