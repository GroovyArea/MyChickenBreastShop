package me.daniel.controller.user;

import me.daniel.domain.UserVO;
import me.daniel.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    // 로그인 페이지 요청
    @GetMapping("/login")
    public String loginPage(){
        return "/fragments/user/login";
    }

    // 로그아웃 처리
    @GetMapping("/logout")
    public String logoutAction(HttpSession session){
        session.invalidate();

        return "redirect:/";
    }

    // 마이페이지 요청
    @GetMapping("/mypage")
    public String mypage(){
        return "/fragments/user/mypage";
    }

    // 로그인 처리
    @PostMapping("/user/login")
    public String loginAction(@ModelAttribute UserVO user, HttpSession session){

        return "redirect:/";
    }

    // 회원 정보 수정

    // 회원 탈퇴 처리

}
