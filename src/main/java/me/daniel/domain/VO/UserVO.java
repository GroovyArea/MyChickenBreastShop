package me.daniel.domain.VO;

import me.daniel.domain.DTO.UserDTO;
import me.daniel.domain.DTO.UserModifyDTO;

public class UserVO {

    private final String userId;
    private final String userPw;
    private final String userName;
    private final String userPhone;
    private final String userEmail;
    private final String userMainAddress;
    private final String userDetailAddress;
    private final String userZipcode;
    private final Integer userGrade;
    private final Integer userReserves;

    public UserVO(String userId, String userPw, String userName, String userPhone, String userEmail, String userMainAddress, String userDetailAddress, String userZipcode, Integer userGrade, Integer userReserves) {
        this.userId = userId;
        this.userPw = userPw;
        this.userName = userName;
        this.userPhone = userPhone;
        this.userEmail = userEmail;
        this.userMainAddress = userMainAddress;
        this.userDetailAddress = userDetailAddress;
        this.userZipcode = userZipcode;
        this.userGrade = userGrade;
        this.userReserves = userReserves;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserPw() {
        return userPw;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserMainAddress() {
        return userMainAddress;
    }

    public String getUserDetailAddress() {
        return userDetailAddress;
    }

    public String getUserZipcode() {
        return userZipcode;
    }

    public Integer getUserGrade() {
        return userGrade;
    }

    public Integer getUserReserves() {
        return userReserves;
    }

}
