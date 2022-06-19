package com.daniel.controller.user;

import com.daniel.domain.DTO.user.UserDTO;
import com.daniel.domain.DTO.user.UserModifyDTO;
import com.daniel.enums.users.UserGrade;
import com.daniel.jwt.AuthorizationExtractor;
import com.daniel.jwt.JwtTokenProvider;
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
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.hamcrest.core.Is.is;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 회원 컨트롤러 mvc 테스트 <Br>
 *
 * <pre>History</pre>
 * 2022.06.16 ver 1.0
 *
 * @author 김남영
 * @version 1.0
 */
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@WebMvcTest(UserController.class)
@Import(value = {AuthorizationExtractor.class, JwtTokenProvider.class})
@AutoConfigureRestDocs
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private RedisService redisService;

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

    final UserModifyDTO modifyUserDTO = UserModifyDTO.builder()
            .userId("aa11")
            .userEmail("095201a@naver.com")
            .userPhone("010-1111-1112")
            .userMainAddress("서울시 서초구")
            .userDetailAddress("서초동")
            .userZipcode("12345")
            .build();

    final UserDTO modifiedUserDTO = UserDTO.builder()
            .userId("aa11")
            .userEmail("095201a@naver.com")
            .userName("테스트회원")
            .userPhone("010-1111-1112")
            .userMainAddress("서울시 서초구")
            .userDetailAddress("서초동")
            .userZipcode("12345")
            .userGrade(UserGrade.BASIC_USER.getValue())
            .userReserves(2000)
            .build();

    @BeforeEach
    public void setUp(RestDocumentationContextProvider restDocumentationContextProvider) {
        mockMvc =
                MockMvcBuilders.standaloneSetup(new UserController(userService))
                        .apply(documentationConfiguration(restDocumentationContextProvider))
                        .addFilters(new CharacterEncodingFilter("utf-8", true))
                        .alwaysDo(document("{method-name}", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())))
                        .build();
    }

    @Test
    @DisplayName("회원 디테일 조회 테스트")
    void detailActionTest() throws Exception {
        Mockito.when(userService.findById(userDTO.getUserId())).thenReturn(userDTO);
        this.mockMvc
                .perform(get("/api/users/{userId}", userDTO.getUserId()).accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer ${AUTH_TOKEN}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.userId", is(userDTO.getUserId())))
                .andExpect(jsonPath("$.data.userMainAddress", is(userDTO.getUserMainAddress())))
                .andExpect(jsonPath("$.message", is("권한 : BASIC_USER")))
                .andDo(document("detailUser", preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())))
        ;
    }

    @Test
    @DisplayName("회원 정보 수정 테스트")
    void modifyActionTest() throws Exception {
        Mockito.when(userService.findById(modifyUserDTO.getUserId())).thenReturn(modifiedUserDTO);

        mockMvc.perform(put("/api/users")
                        .header("Authorization", "Bearer ${AUTH_TOKEN}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.objectToString(modifyUserDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.userMainAddress", is(modifiedUserDTO.getUserMainAddress())))
                .andExpect(jsonPath("$.data.userDetailAddress", is(modifiedUserDTO.getUserDetailAddress())))
                .andExpect(jsonPath("$.message", is("Modify successful")))
                .andDo(document("modifyUser", preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test
    @DisplayName("회원 탈퇴 테스트")
    void deleteActionTest() throws Exception {
        mockMvc.perform(delete("/api/users/{userId}", userDTO.getUserId())
                        .header("Authorization", "Bearer ${AUTH_TOKEN}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Delete successful"))
                .andDo(document("deleteUser", preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));

    }

}