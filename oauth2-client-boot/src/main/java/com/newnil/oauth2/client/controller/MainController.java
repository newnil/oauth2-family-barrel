package com.newnil.oauth2.client.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

	@Autowired
	OAuth2RestTemplate template;
	
	@RequestMapping("/")
	public String homePage(Authentication auth) {
		return "index";
	}

	@RequestMapping({ "/login", "/login.html" })
	public String dashboard() {
		return "redirect:/";
	}

	@RequestMapping("resource.html")
	public String resPage(Model model){
		String data = "NONE";
		data = template.getForObject("http://localhost:8083/res/", String.class);
		model.addAttribute("data", data);
		return "resource";
	}
}
