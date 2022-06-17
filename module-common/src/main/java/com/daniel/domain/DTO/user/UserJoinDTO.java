package com.daniel.domain.DTO.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class UserJoinDTO {

    private String userId;
    private String userPw;
    private String userSalt;
    private String userName;
    private String userPhone;
    private String userEmail;
    private String userMainAddress;
    private String userDetailAddress;
    private String userZipcode;
    private String authKey;

    public void setUserPw(String userPw) {
        this.userPw = userPw;
    }

    public void setUserSalt(String userSalt) {
        this.userSalt = userSalt;
    }
}
