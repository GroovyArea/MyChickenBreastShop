package me.daniel.enums.users;

import java.util.Arrays;
import java.util.Optional;

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

    public static Optional<UserGrade> of(int gradeNumber) {
        return Optional.of(Arrays.stream(UserGrade.values())
                .filter(userGrade1 -> userGrade1.getValue() == gradeNumber)
                .findFirst()
                .orElse(BASIC_USER));
    }
}
