package me.daniel.Enum;

public enum ChickenStatus {

    // 단종, 판매중, 품절
    EXTINCTION(0),
    SALE(1),
    SOLD_OUT(2);

    private final int statusNumber;

    private ChickenStatus(int statusNumber) {
        this.statusNumber = statusNumber;
    }

    public int getValue() {
        return statusNumber;
    }

}
