package me.daniel.controller.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    // 관리자 전용 마이페이지 요청청
   @GetMapping("/mypage")
    public String mypage(){
        return "/fragments/user/mypage";
    }
}
