package com.daniel.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/tete")
    public String hello(){
        return "redirect:/aaa";
    }

    @GetMapping("/aaa")
    public String ghgh(){
        return "굿";
    }
}
