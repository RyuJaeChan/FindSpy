package com.project.chat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {
	
	@GetMapping("/test")
	public String mainPage() {
		System.out.println("Test");
		return "index";
	}
	
}
