package com.daniel.controller.user;

import com.daniel.domain.dto.user.UserDTO;
import com.daniel.domain.dto.user.UserJoinDTO;
import com.daniel.enums.users.UserGrade;
import com.daniel.jwt.AuthorizationExtractor;
import com.daniel.jwt.JwtTokenProvider;
import com.daniel.service.EmailService;
import com.daniel.service.RedisService;
import com.daniel.service.UserService;
import com.daniel.utility.JsonUtil;
import org.junit.jupiter.api.BeforeEach;
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
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.hamcrest.core.Is.is;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 회원가입 컨트롤러 테스트 <br>
 *
 * <pre>History</pre>
 * 김남영, 2022.06.16 ver 1.0
 *
 * @author 김남영
 * @version 1.0
 */
@WebMvcTest(JoinController.class)
@ExtendWith(RestDocumentationExtension.class)
@Import(value = {AuthorizationExtractor.class, JwtTokenProvider.class})
@AutoConfigureRestDocs
class JoinControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @MockBean
    RedisService redisService;

    @MockBean
    EmailService emailService;

    final UserJoinDTO userJoinDTO = UserJoinDTO.builder()
            .userId("aa11")
            .userPw("asdf12")
            .userEmail("095201a@naver.com")
            .userName("테스트회원")
            .userPhone("010-1111-1112")
            .userMainAddress("서울시 동작구")
            .userDetailAddress("사당동")
            .userZipcode("12345")
            .build();

    final UserDTO userDTO = UserDTO.builder()
            .userId("aa11")
            .userEmail("095201a@naver.com")
            .userName("테스트회원")
            .userPhone("010-1111-1112")
            .userMainAddress("서울시 동작구")
            .userDetailAddress("사당동")
            .userZipcode("12345")
            .userGrade(UserGrade.BASIC_USER.getValue())
            .userReserves(2000)
            .build();

    @BeforeEach
    public void setUp(RestDocumentationContextProvider restDocumentationContextProvider) {
        mockMvc =
                MockMvcBuilders.standaloneSetup(new JoinController(userService, emailService))
                        .apply(documentationConfiguration(restDocumentationContextProvider))
                        .addFilters(new CharacterEncodingFilter("utf-8", true))
                        .alwaysDo(document("{method-name}", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())))
                        .build();
    }

    @Test
    @DisplayName("회원가입 테스트")
    void joinActionTest() throws Exception {
        Mockito.when(userService.findById(userJoinDTO.getUserId())).thenReturn(userDTO);
        mockMvc.perform(post("/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.objectToString(userJoinDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Join succeed")))
                .andExpect(jsonPath("$.data.userId", is(userJoinDTO.getUserId())))
                .andExpect(jsonPath("$.data.userEmail", is(userJoinDTO.getUserEmail())))
                .andDo(print())
                .andDo(document("joinActionTest", preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));

    }

    @Test
    @DisplayName("이메일 전송 테스트")
    void authEmailSendActionTest() throws Exception {
        mockMvc.perform(post("/join/email")
                        .content(JsonUtil.objectToString(userJoinDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Email auth key sent"))
                .andDo(print())
                .andDo(document("authEmailSendActionTest", preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }
}