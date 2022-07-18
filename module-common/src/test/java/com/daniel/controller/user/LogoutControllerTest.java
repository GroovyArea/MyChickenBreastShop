package com.daniel.controller.user;

import com.daniel.domain.dto.user.UserLoginDTO;
import com.daniel.jwt.JwtTokenProvider;
import com.daniel.service.RedisService;
import com.daniel.service.UserService;
import com.daniel.utility.JsonUtil;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 로그아웃 컨트롤러 테스트 <br>
 *
 * <pre>History</pre>
 * 김남영, 2022.06.17 ver 1.0
 *
 * @author 김남영
 * @version 1.0
 */
@SpringBootTest(properties = {"spring.config.location = classpath:/application.yml, classpath:/service.yml"})
@AutoConfigureMockMvc
@AutoConfigureRestDocs
class LogoutControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserService userService;

    @Autowired
    RedisService redisService;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    RedisTemplate<String, String> redisTemplate;

    final UserLoginDTO userLoginDTO = UserLoginDTO.builder()
            .userId("aa11")
            .userPw("asdf12")
            .build();

    private String getJwtToken() throws Exception {
        MockHttpServletResponse mockitoResponse = mockMvc.perform(post("/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.objectToString(userLoginDTO)))
                .andDo(print())
                .andReturn()
                .getResponse();

        String response = mockitoResponse.getContentAsString();
        return JsonPath.parse(response).read("$.data");
    }

    @Test
    @DisplayName("로그아웃 테스트")
    void logoutActionTest() throws Exception {
        mockMvc.perform(get("/api/user/logout/" + userLoginDTO.getUserId())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + getJwtToken()))
                .andExpect(status().isOk())
                .andExpect(content().string("Logout succeed"))
                .andDo(print())
                .andDo(document("logoutActionTest", preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));

    }
}