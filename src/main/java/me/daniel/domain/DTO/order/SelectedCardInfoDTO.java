package me.daniel.domain.DTO.order;

public class SelectedCardInfoDTO {

    private String cardBin, cardCorpName, interestFreeInstall;
    private Integer installMonth;

    public SelectedCardInfoDTO(String cardBin, String cardCorpName, String interestFreeInstall, Integer installMonth) {
        this.cardBin = cardBin;
        this.cardCorpName = cardCorpName;
        this.interestFreeInstall = interestFreeInstall;
        this.installMonth = installMonth;
    }

    public String getCardBin() {
        return cardBin;
    }

    public String getCardCorpName() {
        return cardCorpName;
    }

    public String getInterestFreeInstall() {
        return interestFreeInstall;
    }

    public Integer getInstallMonth() {
        return installMonth;
    }
}
