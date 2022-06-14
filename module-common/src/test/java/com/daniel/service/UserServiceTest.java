package com.daniel.service;

import com.daniel.domain.DTO.user.UserLoginDTO;
import com.daniel.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserServiceTest {

    @Autowired
    UserMapper userMapper;

    @Autowired
    UserService userService;

    private UserLoginDTO userDTO;

/*    @BeforeEach
    void setUserDTO(){
        userDTO= new UserLoginDTO();
        userDTO.setUserId("aaa333");
        userDTO.setUserPw("asdf11");
    }

    @Test
    @DisplayName("비밀번호 틀렸을 때")
    void loginAuthTest(){
        assertThatExceptionOfType(LoginAuthFailException.class)
                .isThrownBy(() -> userService.loginAuth(userDTO)).withMessage("비밀번호가 일치하지 않습니다.");
    }*/

}