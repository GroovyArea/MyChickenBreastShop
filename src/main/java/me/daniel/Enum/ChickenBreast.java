package me.daniel.Enum;

public enum ChickenBreast {

    // 스팀, 훈제, 소시지, 스테이크, 볼, 생닭가슴살

    STEAMED(1),
    SMOKED(2),
    SAUSAGE(3),
    STEAK(4),
    BALL(5),
    RAW(6);

    private final int chickenNumber;

    private ChickenBreast(int chickenNumber) {
        this.chickenNumber = chickenNumber;
    }

    public static ChickenBreast of(int chickenNumber){

        switch(chickenNumber){

            case 2 : return SMOKED;
            case 3 : return SAUSAGE;
            case 4 : return STEAK;
            case 5 : return BALL;
            case 6 : return RAW;
            default: return STEAMED;
        }
    }

    public int getValue() {
        return chickenNumber;
    }
}
