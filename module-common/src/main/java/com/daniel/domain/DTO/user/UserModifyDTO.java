package com.daniel.domain.DTO.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@Builder
@NoArgsConstructor
public class UserModifyDTO {

    private String userId;
    private String userPhone;
    private String userEmail;
    private String userMainAddress;
    private String userDetailAddress;
    private String userZipcode;
}
