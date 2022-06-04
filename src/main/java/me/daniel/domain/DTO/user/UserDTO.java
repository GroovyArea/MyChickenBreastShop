package me.daniel.domain.DTO.user;

public class UserDTO {

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
    private Integer userReserves;

    public UserDTO() {
    }

    public UserDTO(String userId, String userPw, String userSalt, String userName, String userPhone, String userEmail, String userMainAddress, String userDetailAddress, String userZipcode, Integer userGrade, Integer userReserves) {
        this.userId = userId;
        this.userPw = userPw;
        this.userSalt = userSalt;
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

    public String getUserSalt() {
        return userSalt;
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
