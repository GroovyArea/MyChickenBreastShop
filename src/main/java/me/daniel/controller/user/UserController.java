package me.daniel.controller.user;

import lombok.RequiredArgsConstructor;
import me.daniel.domain.UserVO;
import me.daniel.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {


    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private final UserService userService;

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
