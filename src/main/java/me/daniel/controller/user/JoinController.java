package me.daniel.controller.user;

import lombok.RequiredArgsConstructor;
import me.daniel.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class JoinController {

    private final UserService userService;


    // 회원 가입 처리
    @PostMapping("/user/join")
    public String joinAction (){

        return "redirect:/";
    }
}