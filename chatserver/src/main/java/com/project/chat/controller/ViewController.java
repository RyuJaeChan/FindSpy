package com.project.chat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.project.chat.entity.User;
import com.project.chat.service.UserService;

@Controller
public class ViewController {
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/test")
	public String mainPage() {
		System.out.println("Test");
		User user2 = new User();
		user2.setName("test name2");
		
		userService.insert(user2);
		return "index";
	}
	
	@GetMapping("/game")
	public String game() {
		
		return "game";
	}
	
	@GetMapping("/chat")
	public String chat() {
		
		return "chat";
	}
	
	
}
