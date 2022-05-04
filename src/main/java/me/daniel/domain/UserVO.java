package me.daniel.domain;

public class UserVO {

    private final String userId;
    private String userPw;
    private final String userName;
    private final String userPhone;
    private final String userEmail;
    private final String userMainAddress;
    private final String userDetailAddress;
    private final String userZipcode;
    private final String userGrade;
    private final String userReserves;

    public UserVO(String userId, String userPw, String userName, String userPhone, String userEmail, String userMainAddress, String userDetailAddress, String userZipcode, String userGrade, String userReserves) {
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

    public String getUserId() { return userId; }

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

    public String getUserGrade() {
        return userGrade;
    }

    public String getUserReserves() {
        return userReserves;
    }

    public void setUserPw(String userPw) {
        this.userPw = userPw;
    }
}
