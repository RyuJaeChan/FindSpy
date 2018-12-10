package com.project.chat.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import com.project.chat.user.User;
import com.project.chat.user.UserService;

@Controller
public class ViewController {
	@GetMapping("/game")
	public String game() {
		
		return "game";
	}
	
	@GetMapping("/chat")
	public String chat() {
		
		return "chat";
	}
	
	@GetMapping("/login")
	public String login() {
		return "login";
	}
	
	@GetMapping("/main")
	public String main(ModelMap modelMap, Principal principal) {
		System.out.println("Test pric : " + principal);
		
		modelMap.put("user", principal);
		
		return "main";
	}
	
	
}
