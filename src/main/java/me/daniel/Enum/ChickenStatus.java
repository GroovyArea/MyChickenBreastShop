package me.daniel.Enum;

public enum ChickenStatus {

    EXTINCTION(0), // 단종
    SALE(1), // 판매중
    SOLD_OUT(2); // 품절

    private final int statusNumber;

    private ChickenStatus(int statusNumber) {
        this.statusNumber = statusNumber;
    }

    public int getValue() {
        return statusNumber;
    }

}
