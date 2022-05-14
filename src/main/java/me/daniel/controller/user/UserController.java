package me.daniel.controller.user;

import me.daniel.jwt.AuthorizationExtractor;
import me.daniel.jwt.JwtTokenProvider;
import me.daniel.domain.DTO.UserDTO;
import me.daniel.domain.DTO.UserModifyDTO;
import me.daniel.enums.ResponseMessage;
import me.daniel.responseMessage.Message;
import me.daniel.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 회원 Controller
 * 회원 조회, 수정, 삭제, 로그인, 로그아웃 요청
 *
 * @author Nam Young Kim
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

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
    public ResponseEntity detailAction(@PathVariable String userId) {
        Message message = new Message
                .Builder(userService.findById(userId))
                .build();

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(message);
    }

    /**
     * 회원 정보 수정
     *
     * @param userModifyDTO 수정할 회원 정보
     * @return ResponseEntity 수정된 회원 정보
     */
    @PutMapping
    public ResponseEntity modifyAction(@ModelAttribute UserModifyDTO userModifyDTO) {
        userService.modifyUser(userModifyDTO);
        Message message = new Message
                .Builder(userService.findById(userModifyDTO.getUserId()))
                .message(ResponseMessage.MODIFY_MESSAGE.getValue())
                .build();

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(message);
    }

    /**
     * 회원 탈퇴 처리
     *
     * @param userId 탈퇴할 회원 아이디
     * @return ResponseEntity 탈퇴 성공 메시지
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity deleteAction(@PathVariable String userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok().body(ResponseMessage.DELETE_MESSAGE.getValue());
    }

}
