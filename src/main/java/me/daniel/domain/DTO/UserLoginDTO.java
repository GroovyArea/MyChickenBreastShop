package me.daniel.domain.DTO;

public class UserLoginDTO {

    private String userId;
    private String userPw;

    public UserLoginDTO(String userId, String userPw) {
        this.userId = userId;
        this.userPw = userPw;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserPw() {
        return userPw;
    }

}
