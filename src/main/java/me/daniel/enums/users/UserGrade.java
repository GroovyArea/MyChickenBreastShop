package me.daniel.enums.users;

public enum UserGrade {

    WITHDRAWAL_USER(0),
    BASIC_USER(1),
    ADMIN(9);

    private final int userGrade;

    private UserGrade(int userGrade) {
        this.userGrade = userGrade;
    }

    public int getValue() {
        return userGrade;
    }

    public static UserGrade of(int gradeNumber) {
        switch (gradeNumber){
            case 0: return WITHDRAWAL_USER;
            case 9: return ADMIN;
            default: return BASIC_USER;
        }
    }
}
