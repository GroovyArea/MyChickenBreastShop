package me.daniel.domain.DTO.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserLoginDTO {

    private final String userId;
    private final String userPw;
}
