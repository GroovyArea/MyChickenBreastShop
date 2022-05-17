package me.daniel.enums.users;

public enum UserGrade {

    WITHDRAWAL(0),
    COMMON(1),
    ADMIN(2);

    private int userGrade;

    private UserGrade(int userGrade) {
        this.userGrade = userGrade;
    }

    public int getValue() {
        return userGrade;
    }
}
