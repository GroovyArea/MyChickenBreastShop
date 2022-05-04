package me.daniel.controller.user;

import me.daniel.domain.UserVO;
import me.daniel.service.UserService;
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
     * @param userVO
     * @param session
     * @return "login Success"
     */
    @PostMapping("/login")
    public String loginAction(@ModelAttribute UserVO userVO, HttpSession session) {
        session.setAttribute("loginUser", userVO);
        return "login Success";
    }

    /**
     * logout 처리
     * @param session
     * @return "logout Success"
     */
    @GetMapping("/logout")
    public String logoutAction(HttpSession session) {
        session.invalidate();
        return "logout Success";
    }


    /**
     * 회원 정보 수정
     * @param userVO
     * @return UserVO
     */
    @PutMapping("/modify")
    public UserVO modifyAction(@ModelAttribute UserVO userVO) {
        userService.modifyUser(userVO);
        return userService.getUser(userVO.getUserId());
    }

    /**
     * 회원 탈퇴 처리
     * @param userId
     * @return "delete Success"
     */
    @DeleteMapping("/delete/{userId}")
    public String deleteAction(@PathVariable String userId) {
        userService.deleteUser(userId);
        return "delete Success";
    }

}
