package me.daniel.domain.DTO;

public class UserListDTO {

    private String userID;
    private String userName;
    private String userPhone;
    private String userEmail;
    private String userMainAddress;
    private String userDetailAddress;
    private String userZipcode;
    private Integer userGrade;

    public UserListDTO(String userID, String userName, String userPhone, String userEmail, String userMainAddress, String userDetailAddress, String userZipcode, Integer userGrade) {
        this.userID = userID;
        this.userName = userName;
        this.userPhone = userPhone;
        this.userEmail = userEmail;
        this.userMainAddress = userMainAddress;
        this.userDetailAddress = userDetailAddress;
        this.userZipcode = userZipcode;
        this.userGrade = userGrade;
    }

    public UserListDTO() {
    }

    public String getUserID() {
        return userID;
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
}
