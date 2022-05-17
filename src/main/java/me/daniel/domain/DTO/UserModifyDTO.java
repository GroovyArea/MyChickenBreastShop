package me.daniel.domain.DTO;

public class UserModifyDTO {

    private final String userId;
    private final String userPhone;
    private final String userEmail;
    private final String userMainAddress;
    private final String userDetailAddress;
    private final String userZipcode;

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
}
