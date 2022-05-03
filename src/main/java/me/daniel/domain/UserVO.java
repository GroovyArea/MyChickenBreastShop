package me.daniel.domain;
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

    public UserVO(int userNo, String userPw, String userName, String userPhone, String userEmail, String userMainAddress, String userDetailAddress, String userZipcode, String userGrade, String userReserves) {
        this.userNo = userNo;
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

    public int getUserNo() {
        return userNo;
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

    public String getUserGrade() {
        return userGrade;
    }

    public String getUserReserves() {
        return userReserves;
    }
}
