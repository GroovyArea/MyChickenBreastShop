package me.daniel.domain.DTO;

public class UserEmailRequestDTO {

    private String userEmail;

    public UserEmailRequestDTO(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public UserEmailRequestDTO() {
    }
}
