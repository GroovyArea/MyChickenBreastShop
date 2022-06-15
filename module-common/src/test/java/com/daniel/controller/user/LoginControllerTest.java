package com.daniel.controller.user;

import com.daniel.domain.DTO.user.UserLoginDTO;
import com.daniel.enums.users.UserGrade;
import com.daniel.exceptions.error.WrongPasswordException;
import com.daniel.jwt.AuthorizationExtractor;
import com.daniel.jwt.JwtTokenProvider;
import com.daniel.mapper.UserMapper;
import com.daniel.service.RedisService;
import com.daniel.service.UserService;
import com.daniel.utility.JsonUtil;
import com.jayway.jsonpath.JsonPath;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * 로그인 컨트롤러 mvc 테스트
 * <p>
 * 2022.06.14 ver 1.0
 *
 * @version 1.0
 */
@WebMvcTest(LoginController.class)
@MockBeans({
        @MockBean(UserService.class),
        @MockBean(RedisService.class),
        @MockBean(JwtTokenProvider.class)
})
@Import(value = {AuthorizationExtractor.class,
        JwtTokenProvider.class,
        RedisService.class})
class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    UserService userService;

    @MockBean
    JwtTokenProvider jwtTokenProvider;

    @MockBean
    UserMapper userMapper;

    private final Logger  log = LoggerFactory.getLogger(this.getClass());

    final UserLoginDTO userLoginDTO = UserLoginDTO.builder()
            .userId("aa11")
            .userPw("asdf12")
            .build();

    final String jwtToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJMb2dpbiBUb2tlbiIsInVzZXJJZCI6ImFhMTEiLCJ1c2VyR3JhZGUiOiJCQVNJQ19VU0VSIiwiaWF0IjoxNjU1Mjc4MjM5LCJleHAiOjE2NTUyODAwMzl9.2ESgVgmrOEl7LWtZ9XV447B9jGuU5wwEidkDDlcghtk";


/*    @BeforeEach
    void mockSetup() {
        LoginController loginController = new LoginController();
        mockMvc = MockMvcBuilders
                .standaloneSetup(loginController)
                .setControllerAdvice(WrongPasswordException.class)
                .setControllerAdvice(LoginFailException.class)
                .setControllerAdvice(WithDrawUserException.class)
                .setControllerAdvice(UserExistsException.class)
                .build();
    }*/

    @Test
    @DisplayName("올바른 아이디 로그인 시 응답 값 검증 테스트")
    void loginTest() throws Exception {

        BDDMockito.when(jwtTokenProvider.createToken(userLoginDTO.getUserId(), String.valueOf(UserGrade.BASIC_USER))).thenReturn(jwtToken);

        MockHttpServletResponse mokitoResponse =  mockMvc.perform(post("/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.objectToString(userLoginDTO)))
                .andExpect(status().isOk())
              .andReturn().getResponse();

        String response = mokitoResponse.getContentAsString();
        String data = JsonPath.parse(response).read("$.data");
        String message = JsonPath.parse(response).read("$.message");

        log.info(data);
        log.info(message);

        Assertions.assertThat(data.equals(jwtToken)).isTrue();
        Assertions.assertThat(message.equals("Login succeed")).isTrue();
    }

    @Test
    @DisplayName("비밀번호 틀렸을 경우")
    void wrongPasswordTest() throws Exception {

        String

        given(userMapper.selectUser(userLoginDTO.getUserId())).willThrow(new WrongPasswordException());



        MvcResult result = mockMvc.perform(post("/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.objectToString(userLoginDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(result1 -> assertTrue(result1.getResolvedException() instanceof WrongPasswordException))
                .andExpect(result1 -> assertEquals("비밀번호가 일치하지 않습니다.",
                        result1.getResolvedException().getMessage()))
                .andReturn();


    }

}