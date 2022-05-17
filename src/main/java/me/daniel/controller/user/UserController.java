package me.daniel.controller.user;

import me.daniel.domain.DTO.UserDTO;
import me.daniel.domain.DTO.UserModifyDTO;
import me.daniel.enums.global.ResponseMessage;
import me.daniel.enums.users.UserGrade;
import me.daniel.responseMessage.Message;
import me.daniel.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 회원 Controller
 * 회원 조회, 수정, 삭제
 *
 * @author Nam Young Kim
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private static final String PASSWORD_ENCRYPT = "암호화";

    private final UserService userService;
    /**
     * 회원 탈퇴 정보를 담기 위한 map
     */
    private Map<String, Object> deleteUserMap = new HashMap<>();

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 회원 정보 디테일 조회
     *
     * @param userId 회원 아이디
     * @return ResponseEntity 회원 정보
     */
    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> detailAction(@PathVariable String userId) {
        UserDTO userDTO = userService.findById(userId);
        userDTO.setUserPw(PASSWORD_ENCRYPT);
        userDTO.setUserSalt(PASSWORD_ENCRYPT);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(userDTO);
    }

    /**
     * 회원 정보 수정
     *
     * @param userModifyDTO 수정할 회원 정보
     * @return Message 수정된 회원 정보
     */
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
    @DeleteMapping("/{userId}")
    public ResponseEntity deleteAction(@PathVariable String userId) {
        deleteUserMap.put("userId", userId);
        deleteUserMap.put("userGrade", UserGrade.WITHDRAWAL.getValue());
        userService.deleteUser(deleteUserMap);
        return ResponseEntity.ok().body(ResponseMessage.DELETE_MESSAGE.getValue());
    }

}
