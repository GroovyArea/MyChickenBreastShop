package me.daniel.controller.user;

import me.daniel.domain.UserVO;
import me.daniel.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * 회원 Controller
 * 회원 조회, 수정, 삭제, 로그인, 로그아웃 요청
 *
 * @author Nam Young Kim
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * login 처리
     *
     * @param userVO
     * @param session
     * @return ResponseEntity 로그인 성공 메시지
     */
    @PostMapping("/login")
    public ResponseEntity loginAction(@ModelAttribute UserVO userVO, HttpSession session) {
        session.setAttribute("loginUser", userVO);
        return ResponseEntity.ok().body("login succeed!");
    }

    /**
     * logout 처리
     *
     * @param session
     * @return ResponseEntity 로그아웃 성공 메시지
     */
    @GetMapping("/logout")
    public ResponseEntity logoutAction(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok().body("logout succeed!");
    }

    /**
     * 회원 정보 디테일 조회
     *
     * @param userId 회원 아이디
     * @return ResponseEntity 회원 정보
     */
    @GetMapping("/detail/{userId}")
    public ResponseEntity detailAction(@PathVariable String userId) {
        UserVO returnUser = userService.getUser(userId);
        returnUser.setUserPw("암호화");
        return ResponseEntity.ok().body(returnUser);
    }

    /**
     * 회원 정보 수정
     *
     * @param userVO 수정할 회원 정보
     * @return ResponseEntity 수정된 회원 정보
     */
    @PostMapping("/modify")
    public ResponseEntity modifyAction(@ModelAttribute UserVO userVO) {
        userService.modifyUser(userVO);
        UserVO returnUser = userService.getUser(userVO.getUserId());
        returnUser.setUserPw("암호화");
        return ResponseEntity.ok().body(returnUser);
    }

    /**
     * 회원 탈퇴 처리
     *
     * @param userId 탈퇴할 회원 아이디
     * @return ResponseEntity 탈퇴 성공 메시지
     */
    @GetMapping("/delete/{userId}")
    public ResponseEntity deleteAction(@PathVariable String userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok().body("delete succeed");
    }

}
