package com.daniel.controller.user;

import com.daniel.domain.DTO.user.UserLoginDTO;
import com.daniel.jwt.AuthorizationExtractor;
import com.daniel.jwt.JwtTokenProvider;
import com.daniel.service.RedisService;
import com.daniel.service.UserService;
import com.daniel.utility.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * 로그인 컨트롤러 mvc 테스트
 * <p>
 * 2022.06.14 ver 1.0
 *
 * @version 1.0
 */

@ExtendWith(SpringExtension.class)
@WebMvcTest(LoginController.class)
@Slf4j
@Import(value = {AuthorizationExtractor.class})
@AutoConfigureRestDocs
class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    UserService userService;

    @MockBean
    RedisService redisService;

    @MockBean
    JwtTokenProvider jwtTokenProvider;

    final UserLoginDTO userLoginDTO = UserLoginDTO.builder()
            .userId("aa11")
            .userPw("asdf12")
            .build();

    final String jwtToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJMb2dpbiBUb2tlbiIsInVzZXJJZCI6ImFhMTEiLCJ1c2VyR3JhZGUiOiJCQVNJQ19VU0VSIiwiaWF0IjoxNjU1Mjc4MjM5LCJleHAiOjE2NTUyODAwMzl9.2ESgVgmrOEl7LWtZ9XV447B9jGuU5wwEidkDDlcghtk";

    @Test
    @DisplayName("올바른 아이디 로그인 시 응답 값 검증 테스트")
    void loginTest() throws Exception {
        Mockito.when(userService.createToken(any())).thenReturn(jwtToken);

        mockMvc.perform(post("/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.objectToString(userLoginDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.message", is("Login succeed")))
                .andDo(print())
                .andDo(document("loginTest", preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));

    }

}