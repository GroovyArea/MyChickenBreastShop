package com.daniel.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class UserVO {

    private String userId;
    private String userPw;
    private String userSalt;
    private String userName;
    private String userPhone;
    private String userEmail;
    private String userMainAddress;
    private String userDetailAddress;
    private String userZipcode;
    private Integer userGrade;
}
