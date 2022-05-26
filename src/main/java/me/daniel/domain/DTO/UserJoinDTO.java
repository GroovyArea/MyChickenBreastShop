package me.daniel.domain.DTO;

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

    public UserJoinDTO(String userId, String userPw, String userSalt, String userName, String userPhone, String userEmail, String userMainAddress, String userDetailAddress, String userZipcode, String authKey) {
        this.userId = userId;
        this.userPw = userPw;
        this.userSalt = userSalt;
        this.userName = userName;
        this.userPhone = userPhone;
        this.userEmail = userEmail;
        this.userMainAddress = userMainAddress;
        this.userDetailAddress = userDetailAddress;
        this.userZipcode = userZipcode;
        this.authKey = authKey;
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

    public String getAuthKey() {
        return authKey;
    }

    public String getUserSalt() {
        return userSalt;
    }

    public void setUserPw(String userPw) {
        this.userPw = userPw;
    }

    public void setUserSalt(String userSalt) {
        this.userSalt = userSalt;
    }
}
