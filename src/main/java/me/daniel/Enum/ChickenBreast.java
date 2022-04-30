package me.daniel.Enum;

public enum ChickenBreast {

    STEAMED(1),
    SMOKED(2),
    LOW_SALT(3),
    STEAK(4),
    CUBE(5),
    RAW(6);

    private final int chickenNumber;

    private ChickenBreast(int chickenNumber) {
        this.chickenNumber = chickenNumber;
    }

    public int getValue() {
        return chickenNumber;
    }
}
