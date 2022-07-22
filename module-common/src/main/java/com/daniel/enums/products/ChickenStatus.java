package com.daniel.enums.products;

import java.util.Arrays;

public enum ChickenStatus {

    EXTINCTION(0), // 단종
    SALE(1), // 판매중
    SOLD_OUT(2); // 품절

    private final int statusNumber;

    ChickenStatus(int statusNumber) {
        this.statusNumber = statusNumber;
    }

    public static ChickenStatus of(int status) {
        return Arrays.stream(ChickenStatus.values())
                .filter(chickenStatus -> chickenStatus.getValue() == status)
                .findAny()
                .orElse(SALE);
    }

    public int getValue() {
        return statusNumber;
    }

}
