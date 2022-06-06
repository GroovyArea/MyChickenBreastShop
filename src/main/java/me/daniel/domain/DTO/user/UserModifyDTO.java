package me.daniel.domain.DTO.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserModifyDTO {

    private final String userId;
    private final String userPhone;
    private final String userEmail;
    private final String userMainAddress;
    private final String userDetailAddress;
    private final String userZipcode;
}
