package me.daniel.domain.DTO;

import me.daniel.domain.VO.UserVO;

public class UserModifyDTO {

    private String userId;
    private String userPhone;
    private String userEmail;
    private String userMainAddress;
    private String userDetailAddress;
    private String userZipcode;

    public UserModifyDTO(String userId, String userPhone, String userEmail, String userMainAddress, String userDetailAddress, String userZipcode) {
        this.userId = userId;
        this.userPhone = userPhone;
        this.userEmail = userEmail;
        this.userMainAddress = userMainAddress;
        this.userDetailAddress = userDetailAddress;
        this.userZipcode = userZipcode;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserMainAddress() {
        return userMainAddress;
    }

    public void setUserMainAddress(String userMainAddress) {
        this.userMainAddress = userMainAddress;
    }

    public String getUserDetailAddress() {
        return userDetailAddress;
    }

    public void setUserDetailAddress(String userDetailAddress) {
        this.userDetailAddress = userDetailAddress;
    }

    public String getUserZipcode() {
        return userZipcode;
    }

    public void setUserZipcode(String userZipcode) {
        this.userZipcode = userZipcode;
    }
}
