package me.daniel.controller;

import lombok.RequiredArgsConstructor;
import me.daniel.domain.UserVO;
import me.daniel.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UserService userService;

    @GetMapping("/")
    public String hello(HttpSession session){

        session.setAttribute("loginUser", userService.getUser(1));
        logger.info(((UserVO)session.getAttribute("loginUser")).getUserName());
        logger.info(((UserVO)session.getAttribute("loginUser")).getUserGrade());

        return "index";
    }
}
