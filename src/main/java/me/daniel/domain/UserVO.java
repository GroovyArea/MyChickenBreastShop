package me.daniel.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserVO {

    private int userNo;
    private String userPw;
    private String userName;
    private String userPhone;
    private String userEmail;
    private String userMainAddress;
    private String userDetailAddress;
    private String userZipcode;
    private String userGrade;
    private String userReserves;

    public UserVO(int userNo, String userName){
        this.userNo = userNo;
        this.userName = userName;
    }
}
