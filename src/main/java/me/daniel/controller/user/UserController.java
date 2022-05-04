package me.daniel.controller.user;

import me.daniel.domain.UserVO;
import me.daniel.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 로그아웃 처리
    @GetMapping("/logout")
    public String logoutAction(HttpSession session) {
        session.invalidate();
        return "logout Success";
    }

    // 로그인 처리
    @PostMapping("/login")
    public String loginAction(@ModelAttribute UserVO userVO, HttpSession session) {
        session.setAttribute("loginUser", userVO);
        return "login Success";
    }

    // 회원 정보 수정
    @PutMapping("/modify")
    public UserVO modifyAction(@ModelAttribute UserVO userVO) {
        userService.modifyUser(userVO);
        return userService.getUser(userVO.getUserId());
    }

    // 회원 탈퇴 처리
    @DeleteMapping("/delete/{userId}")
    public String deleteAction(@PathVariable String userId) {
        userService.deleteUser(userId);
        return "delete Success";
    }

}
