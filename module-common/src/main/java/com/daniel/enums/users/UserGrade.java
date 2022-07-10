package com.daniel.enums.users;

import java.util.Arrays;
import java.util.Optional;

public enum UserGrade {

    WITHDRAWAL_USER(0),
    BASIC_USER(1),
    ADMIN(9);

    private final int grade;

    UserGrade(int grade) {
        this.grade = grade;
    }

    public int getValue() {
        return grade;
    }

    public static Optional<UserGrade> of(int gradeNumber) {
        return Optional.of(Arrays.stream(UserGrade.values())
                .filter(userGrade -> userGrade.getValue() == gradeNumber)
                .findFirst()
                .orElse(BASIC_USER));
    }
}
