package com.newnil.cas.oauth2.provider.controller;

import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

	@RequestMapping("/")
	public String homePage() {
		return "index";
	}

	@RequestMapping("/login.html")
	public String loginPage() {
		return "login";
	}

}
