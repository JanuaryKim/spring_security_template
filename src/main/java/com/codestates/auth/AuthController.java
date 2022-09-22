package com.codestates.auth;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auths")
public class AuthController {
    @GetMapping("/login-form")
    public String loginForm() {
        return "login";
    }


    @PostMapping("/login")
    public String login() {
        System.out.println("Login successfully!");
        return "home";
    }

    @GetMapping("/access-denied")
    public String accessDenied() {

        System.out.println("접근 불가");
        return "access-denied";
    }
}
