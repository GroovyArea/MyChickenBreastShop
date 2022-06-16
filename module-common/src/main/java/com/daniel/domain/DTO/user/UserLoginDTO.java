package com.daniel.domain.DTO.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class UserLoginDTO {

    private final String userId;
    private final String userPw;
}
