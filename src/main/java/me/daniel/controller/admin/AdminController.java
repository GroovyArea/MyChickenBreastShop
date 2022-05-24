package me.daniel.controller.admin;

import me.daniel.interceptor.auth.Auth;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Auth(role = Auth.Role.ADMIN)
    @GetMapping
    public String adminTest() {
        return "관리자다.";
    }
}
