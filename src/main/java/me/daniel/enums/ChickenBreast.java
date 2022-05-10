package me.daniel.enums;

public enum ChickenBreast {

    STEAMED(1), // 스팀
    SMOKED(2), // 훈제
    SAUSAGE(3), // 소시지
    STEAK(4), // 스테이크
    BALL(5), // 볼
    RAW(6); // 생닭가슴살

    private final int chickenNumber;

    private ChickenBreast(int chickenNumber) {
        this.chickenNumber = chickenNumber;
    }

    public static ChickenBreast of(int chickenNumber) {
        switch (chickenNumber) {
            case 2:
                return SMOKED;
            case 3:
                return SAUSAGE;
            case 4:
                return STEAK;
            case 5:
                return BALL;
            case 6:
                return RAW;
            default:
                return STEAMED;
        }
    }

    public int getValue() {
        return chickenNumber;
    }
}
