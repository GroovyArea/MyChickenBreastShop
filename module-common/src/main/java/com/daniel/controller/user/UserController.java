package com.daniel.controller.user;

import com.daniel.domain.DTO.user.UserDTO;
import com.daniel.domain.DTO.user.UserModifyDTO;
import com.daniel.enums.global.ResponseMessage;
import com.daniel.enums.users.UserGrade;
import com.daniel.interceptor.auth.Auth;
import com.daniel.responseMessage.Message;
import com.daniel.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 회원 Controller <br>
 * 회원 조회, 수정, 삭제
 *
 * @author Nam Young Kim
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    /**
     * 회원 탈퇴 정보를 담기 위한 map
     */
    private final Map<String, Object> deleteUserMap = new HashMap<>();

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 회원 정보 디테일 조회
     *
     * @param userId 회원 아이디
     * @return ResponseEntity 회원 정보
     */
    @Auth(role = Auth.Role.BASIC_USER)
    @GetMapping("/{userId}")
    public Message detailAction(@PathVariable String userId) {
        UserDTO userDTO = userService.findById(userId);
        return new Message
                .Builder(userDTO)
                .message("권한 : " + UserGrade.of(userDTO.getUserGrade()).orElse(UserGrade.BASIC_USER))
                .mediaType(MediaType.APPLICATION_JSON)
                .httpStatus(HttpStatus.OK)
                .build();
    }

    /**
     * 회원 정보 수정
     *
     * @param userModifyDTO 수정할 회원 정보
     * @return Message 수정된 회원 정보
     */
    @Auth(role = Auth.Role.BASIC_USER)
    @PutMapping
    public Message modifyAction(@ModelAttribute UserModifyDTO userModifyDTO) {
        userService.modifyUser(userModifyDTO);
        return new Message
                .Builder(userService.findById(userModifyDTO.getUserId()))
                .message(ResponseMessage.MODIFY_MESSAGE.getValue())
                .mediaType(MediaType.APPLICATION_JSON)
                .httpStatus(HttpStatus.OK)
                .build();
    }

    /**
     * 회원 탈퇴 처리
     *
     * @param userId 탈퇴할 회원 아이디
     * @return ResponseEntity 탈퇴 성공 메시지
     */
    @Auth(role = Auth.Role.BASIC_USER)
    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteAction(@PathVariable String userId) {
        deleteUserMap.put("userId", userId);
        deleteUserMap.put("userGrade", UserGrade.WITHDRAWAL_USER.getValue());
        userService.changeGradeUser(deleteUserMap);
        return ResponseEntity.ok().body(ResponseMessage.DELETE_MESSAGE.getValue());
    }

}
