package com.newnil.cas.oauth2.provider.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class DashboardController {

    @GetMapping
    public String indexPage() {
        return "index";
    }

    @GetMapping("/login.html")
    public String loginPage() {
        return "login";
    }

}
