package me.daniel.domain.VO;

public class UserVO {

    private String userId;
    private String userPw;
    private String userName;
    private String userPhone;
    private String userEmail;
    private String userMainAddress;
    private String userDetailAddress;
    private String userZipcode;
    private Integer userGrade;
    private Integer userReserves;
    private String userSalt;

    public UserVO(String userId, String userPw, String userName, String userPhone, String userEmail, String userMainAddress, String userDetailAddress, String userZipcode, Integer userGrade, Integer userReserves, String userSalt) {
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
        this.userSalt = userSalt;
    }

    public UserVO() {
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

    public String getUserSalt() {
        return userSalt;
    }
}
